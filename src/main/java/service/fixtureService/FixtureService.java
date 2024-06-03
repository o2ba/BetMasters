package service.fixtureService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import dto.request.ApiRequest;
import dto.request.MetricsRequest;
import common.object.fixture.FixtureBetsObject;
import common.object.fixture.FixtureObject;
import dto.response.FixtureOddsResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FixtureService {

    /**
     * The telemetry client for sending telemetry data to Application Insights.
     */
    private final @NotNull TelemetryClient telemetryClient;

    /**
     * The error message returned by the API.
     */
    private @Nullable String errors;

    /**
     * Logger for the FixtureService class.
     */
    private static final Logger logger = LoggerFactory.getLogger(FixtureService.class);

    /**
     * The parameters to send with the request.
     */
    private final Map<String, String> params;

    /**
     *  boolean to filter out past games
     */
    private final boolean futureGamesOnly;

    /**
     * The response headers
     */
    private @Nullable Map<String, List<String>> responseHeaders;

    /**
     * The response code returned by the API.
     */
    private int responseCode;

    /**
     * Constructor for the FixtureService class
     *
     * @param params          the parameters to send with the request. As this is a hybrid request for both odds and fixtures.
     *                        The param "bookmaker" is required. params "league" and "season" are interdependent. param
     *                        "timezone" is recommended.
     * @param futureGamesOnly whether to get future fixtures or not. If true, past fixtures are filtered out.
     * @throws IllegalArgumentException if the parameters are invalid. 'bookmaker' must be present and league and
     *                                  season are either both present or both absent.
     */
    public FixtureService(Map<String, String> params, boolean futureGamesOnly) throws IllegalArgumentException {

        if (!isValidParameters(params)) {
            throw new IllegalArgumentException("Invalid parameters. Please ensure that 'bookmaker' is present and "
                    + "'league' and 'season' are either both present or both absent.");
        }

        this.params = params;
        this.futureGamesOnly = futureGamesOnly;

        this.telemetryClient = MetricsRequest.getInstance().getTelemetryClient();

    }

    // ----------------- Interface ----------------- //

    /**
     * Builds the request for the fixtures. Logs the request sent, time taken and any errors.
     * Returns a list of objects of the fixtures.
     *
     * @return FixtureOddResponse of fixtures
     */
    public @NotNull FixtureOddsResponse<FixtureObject> getFixtures() {

        Map<String, String> fixtureParams = new HashMap<>(Map.copyOf(params));
        fixtureParams.remove("bookmaker");

        long timeFootballRequest = System.currentTimeMillis();
        logger.debug("Sending request to [FixtureObject] with parameters: {}", fixtureParams);

        timeFootballRequest = System.currentTimeMillis() - timeFootballRequest;

        FixtureOddsResponse<FixtureObject> response = sendRequest("fixtures", fixtureParams, FixtureObject::new);

        logger.debug("Request to [FixtureObject] came back with %d results with response code %s and took %d milliseconds.".formatted(response.getAmountOfResults(), response.getResponseCode(), timeFootballRequest));

        if (!response.getError().isEmpty()) {
            logger.error("Error in the request for fixtures. " + "Error: {}", response.getError().getAsJsonArray().toString());
        }

        if (futureGamesOnly) {
            response.getPayload().removeIf(FixtureObject::gameInPast);
        }

        return response;
    }

    /**
     * Builds the request for the fixtures bets. Logs the request sent, time taken and any errors.
     * Returns a list of objects of the fixtures bets.
     *
     * @return FixtureOddResponse of fixture bets
     */
    public @NotNull FixtureOddsResponse<FixtureBetsObject> getFixturesBets() {

        long timeFootballRequest = System.currentTimeMillis();
        logger.debug("Sending request to [Odds] with parameters: {}", betsParams());

        FixtureOddsResponse<FixtureBetsObject> response = sendRequest("odds", betsParams(), FixtureBetsObject::new);

        timeFootballRequest = System.currentTimeMillis() - timeFootballRequest;

        logger.debug("Request to [Odds] came back with %d results with response code %s and took %d milliseconds.".formatted(response.getAmountOfResults(), response.getResponseCode(), timeFootballRequest));

        if (!response.getError().isEmpty()) {
            logger.error("Error in the request for odds. " + "Error: {}", response.getError().getAsJsonArray().toString());
        }

        if (response.getTotalPages() > 1) {
            List<FixtureBetsObject> allBets = new ArrayList<>(response.getPayload());

            for (int i = 2; i <= response.getTotalPages(); i++) {
                Map<String, String> mutableParams = new HashMap<>(this.betsParams());
                mutableParams.put("page", String.valueOf(i));

                logger.debug("Sending request to [Odds] for page %d/%d with parameters: ".formatted(i, response.getTotalPages()) + mutableParams);

                allBets.addAll(sendRequest("odds", mutableParams, FixtureBetsObject::new).getPayload());

                logger.debug(("Request to [Odds] for page %d/%d came back with %d results with response code %s and " + "took %d milliseconds.").formatted(i, response.getTotalPages(), response.getAmountOfResults(), response.getResponseCode(), timeFootballRequest));
            }

            response.setPayload(allBets);
        }


        return response;
    }

    /**
     * Builds the request for the fixtures. Logs the request sent, time taken and any errors, and sends telemetry data.
     * Returns a list of objects of the fixtures with odds.
     *
     * @param gamesWithBetsOnly whether to get fixtures with bets only or not.
     *                          If true, fixtures without bets are filtered out. The bets are contained
     *                          in the FixtureObject class as a FixtureBetsController object.
     * @return a List of Fixtures objects is returned, with the FixtureBetsController defined for the respective Fixtures if
     * they exist. Please refer to the FixtureObject class for more information.
     */
    public @NotNull List<FixtureObject> getFixturesWithOdds(boolean gamesWithBetsOnly) {

        long timer = System.currentTimeMillis();

        FixtureOddsResponse<FixtureObject> fixturesResponse = getFixtures();
        List<FixtureObject> fixtures = fixturesResponse.getPayload();

        FixtureOddsResponse<FixtureBetsObject> betsResponse = getFixturesBets();
        List<FixtureBetsObject> fixtureBetObjects = betsResponse.getPayload();

        for (FixtureObject fixture : fixtures) {
            for (FixtureBetsObject bet : fixtureBetObjects) {
                if (bet.getFixtureId().equals(fixture.getFixtureId())) {
                    fixture.setFixtureBets(bet);
                    break;
                }
            }
        }

        if (gamesWithBetsOnly) {
            fixtures.removeIf(fixture -> !fixture.gameWithOdds());
        }

        Long entireRequestTime = System.currentTimeMillis() - timer;
        Long fixturesResponseConstructorTime = fixturesResponse.getConstructorTime();
        Long betsResponseConstructorTime = betsResponse.getConstructorTime();

        sendTelemetryDataGetFixtureWithOdds(params,
                entireRequestTime,
                fixturesResponseConstructorTime,
                betsResponseConstructorTime);

        return fixtures;
    }

    /**
     * Builds a JSON Array of the fixtures. Supports both fixtures and fixtures with bets.
     *
     * @return the JSON Array of the fixtures.
     */
    public JsonArray toJsonFixtures(List<FixtureObject> fixtures) {
        return fixtures.stream().map(FixtureObject::toJson).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    /**
     * Builds a JSON Array of the bets.
     *
     * @return the JSON Array of the bets.
     */
    public JsonArray toJsonBets(List<FixtureBetsObject> bets) {
        return bets.stream().map(FixtureBetsObject::toJson).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    // ----------------- Private Methods ----------------- //

    /**
     * Checks if the parameters are valid
     *
     * @param params the parameters to check
     * @return true if the parameters are valid, false otherwise
     */
    private boolean isValidParameters(Map<String, String> params) {

        if (!params.containsKey("bookmaker")) {
            return false;
        }

        return (!params.containsKey("league") || params.containsKey("season")) && (params.containsKey("league") || !params.containsKey("season"));
    }


    /**
     * Builds a request to Odds which is accepted. Involves removing unsupported parameters.
     * @return the parameters to send with the request
     */
    private Map<String, String> betsParams() {

        Map<String, String> oddsParams = new HashMap<>(this.params);

        if (params.containsKey("id")) {
            oddsParams.put("fixture", oddsParams.get("id"));
            oddsParams.remove("id");
        }
        oddsParams.remove("from");
        oddsParams.remove("to");
        return oddsParams;
    }

    /**
     * Sends a request to the API and returns the response.
     * The response is converted to the desired type using the converter function. The response time is also recorded.
     * For this class, the method is always GET.
     * @param endpoint the endpoint to send the request to
     * @param params the parameters to send with the request
     * @param converter the function to convert the response to the desired type
     * @throws RuntimeException if the token is inaccessible
     * @throws IllegalStateException if the request fails
     * @param <T> the type of the response
     */
    private <T> FixtureOddsResponse<T> sendRequest(String endpoint, Map<String, String> params,
                                                   Function<JsonElement, T> converter) {
        String method = "GET";

        ApiRequest request = new ApiRequest();
        request.sendRequest(endpoint, method, params);
        JsonElement fixtures = request.sendRequest(endpoint, method, params).getAsJsonObject();

        assert request.getResponseHeaders() != null;
        this.responseHeaders = request.getResponseHeaders();

        FixtureOddsResponse<T> response = new FixtureOddsResponse<>(fixtures, converter, request.getResponseCode(),
                responseHeaders);

        errors = response.getError().toString();

        Map<String, List<String>> headers = response.getResponseHeaders();


        return response;
    }


    /**
     * Sends telemetry data to Application Insights for the request to get fixtures with odds.
     * @param params parameters to include in the telemetry data
     * @param entireRequestTime the time taken for the entire request
     * @param fixturesResponseConstructorTime the time taken to construct the fixtures' response. Involves converting the
     *                                        JsonElement into objects of type FixtureObject.
     * @param betsResponseConstructorTime the time taken to construct the bets' response. Involves converting the
     *                                    JsonElement into objects of type T.
     */
    private void sendTelemetryDataGetFixtureWithOdds(Map<String, String> params,
                                                     Long entireRequestTime,
                                                     Long fixturesResponseConstructorTime,
                                                     Long betsResponseConstructorTime) {
        RemoteDependencyTelemetry dependencyTelemetry = new RemoteDependencyTelemetry();
        dependencyTelemetry.setName("GetFixtureWithOdds Request");
        dependencyTelemetry.setType("HTTP");
        dependencyTelemetry.getProperties().put("errors", errors);


        if (responseHeaders != null) {
            responseHeaders.forEach((key, value) -> dependencyTelemetry.getProperties()
                    .put("header_" + key, value.toString()));
        }

        if (params != null) {
            params.forEach((key, value) -> dependencyTelemetry.getProperties().put("param_" + key, value));
        }

        telemetryClient.trackDependency(dependencyTelemetry);

        telemetryClient.trackMetric("Entire Request Time", entireRequestTime);
        telemetryClient.trackMetric("Fixtures Response Constructor Time", fixturesResponseConstructorTime);
        telemetryClient.trackMetric("Bets Response Constructor Time", betsResponseConstructorTime);
        telemetryClient.trackMetric("JSON -> Object Conversion Time",
                entireRequestTime -
                fixturesResponseConstructorTime -
                betsResponseConstructorTime);
    }

}
