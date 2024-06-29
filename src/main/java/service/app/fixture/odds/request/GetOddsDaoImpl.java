package service.app.fixture.v2.odds.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import service.app.fixture.v2.common.exception.RequestSendingException;
import service.general.external.apiService.ApiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.app.fixture.v2.common.model.FootballResponse;

import java.util.Map;

@Service
public class GetOddsDaoImpl implements GetOddsDao {

    ApiRequest apiRequest;

    @Autowired
    public GetOddsDaoImpl(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    @Value("${bookmaker}")
    private int bookmaker;

    private String apiKey = System.getenv("API_KEY");

    /**
     * Retrieve odds for a fixture. Uses the default bookmaker
     *
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID     The ID of the odd to get
     * @return Map<String, Double>
     */
    @Override
    public FootballResponse getOdds(int fixtureId, int oddID) throws RequestSendingException {

        System.out.println();

        return getOdds(fixtureId, oddID, String.valueOf(bookmaker));
    }

    /**
     * Retrieve odds for a fixture
     *
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID     The ID of the odd to get
     * @param bookmaker The bookmaker to get the odds from
     * @return Map<String, Double>
     */
    @Override
    public FootballResponse getOdds(int fixtureId, int oddID, String bookmaker) throws RequestSendingException {
        Map<String, String> params = Map.of("fixture", String.valueOf(fixtureId), "bet", String.valueOf(oddID), "bookmaker",
                bookmaker);

        JsonElement response = apiRequest.sendRequest("odds", "GET", params);


        return new FootballResponse(response.getAsJsonObject().get("response").getAsJsonArray(),
                response.getAsJsonObject().get("errors").getAsJsonArray(),
                response.getAsJsonObject().get("results").getAsInt());
    }

    /**
     * Retrieve odds for a league and season
     *
     * @param leagueId The ID of the league to get the odds for
     * @param season   The season to get the odds for
     * @return Map<Integer, Map < String, Double>>
     */
    @Override
    public FootballResponse getOddsForLeagueAndSeason(int leagueId, int season, int oddID) throws RequestSendingException {
        return getOddsForLeagueAndSeason(leagueId, season, String.valueOf(bookmaker), oddID);
    }

    /**
     * Retrieve odds for a league and season
     *
     * @param leagueId  The ID of the league to get the odds for
     * @param season    The season to get the odds for
     * @param bookmaker The bookmaker to get the odds from
     * @return Map<Integer, Map < String, Double>>
     */
    @Override
    public FootballResponse getOddsForLeagueAndSeason(int leagueId, int season, String bookmaker, int oddID) throws RequestSendingException {
        Map<String, String> params = Map.of("league", String.valueOf(leagueId), "season", String.valueOf(season),
                "bookmaker", bookmaker);

        JsonElement response = apiRequest.sendRequest("odds", "GET", params);

        // pagination
        int totalPages = response.getAsJsonObject().get("paging").getAsJsonObject().get("total").getAsInt();
        JsonArray odds = response.getAsJsonObject().get("response").getAsJsonArray();
        System.out.println("Getting page 1");

        if (totalPages > 1) {
            for (int i = 2; i <= totalPages; i++) {
                System.out.println("Getting page " + i);
                params = Map.of("league", String.valueOf(leagueId), "season", String.valueOf(season),
                        "bookmaker", bookmaker, "page", String.valueOf(i));
                response = apiRequest.sendRequest("odds", "GET", params);

                odds.addAll(response.getAsJsonObject().get("response").getAsJsonArray());
            }
        }

        return new FootballResponse(odds,
                response.getAsJsonObject().get("errors").getAsJsonArray(),
                response.getAsJsonObject().get("results").getAsInt());
    }
}
