package service.app.footballService.fixtureOdds.fixture;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.exception.InternalServerError;
import common.model.football.fixture.Fixture;
import dto.request.httpRequest.exception.InvalidMethodException;
import dto.request.httpRequest.exception.RequestSendingException;
import dto.request.httpRequest.exception.ResponseParseException;
import dto.request.FootballApiRequest;
import dto.response.model.FootballResponsePayload;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.footballService.fixtureOdds.exception.InvalidResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple class to get fixtures from the API
 * Does not include odds in the fixtures
 */
@Service
public class GetFixturesService {
    private final FootballApiRequest footballApiRequest;
    private final Gson gson;

    @Autowired
    public GetFixturesService(FootballApiRequest footballApiRequest) {
        this.footballApiRequest = footballApiRequest;
        this.gson = new Gson();
    }

    public List<Fixture> getFixtures(Map<String, String> params)
    throws InternalServerError, InvalidResponseException {

        FootballResponsePayload response;

        try {
            response = footballApiRequest.sendRequest("fixtures", "GET", params, null);

            // Check if the response is valid
            try (Response rawResponse = response.rawResponse()){
                if (rawResponse.body() == null || rawResponse.code() != 200) {
                    throw new InvalidResponseException("Invalid Response: Response code is " + rawResponse.code());
                }
            }
        } catch (RequestSendingException | InvalidMethodException | ResponseParseException e) {
            throw new InternalServerError("Error sending request to the server");
        }

        JsonElement jsonResponse = response.jsonResponse();

        List<Fixture> fixtures = new ArrayList<>();

        if (jsonResponse.isJsonObject()) {
            JsonObject responseObject = jsonResponse.getAsJsonObject();
            JsonArray fixturesArray = responseObject.getAsJsonArray("response");
            for (JsonElement fixtureElement : fixturesArray) {
                Fixture fixture = gson.fromJson(fixtureElement, Fixture.class);
                fixtures.add(fixture);
            }
        }

        return fixtures;

    }
}
