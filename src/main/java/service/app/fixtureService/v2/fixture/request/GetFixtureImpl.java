package service.app.fixtureService.v2.fixture.request;

import dto.ApiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.fixtureService.v2.common.exception.FootballApiException;
import service.app.fixtureService.v2.common.model.FootballResponse;

import java.util.Map;

@Service
public class GetFixtureImpl implements GetFixture {


    ApiRequest apiRequest;

    @Autowired
    public GetFixtureImpl(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    /**
     * Retrieve fixture by fixtureId
     *
     * @param fixtureId The ID of the fixture to get
     * @return FootballResponse
     * @throws FootballApiException if the request fails
     */
    @Override
    public FootballResponse getFixtureById(int fixtureId) throws FootballApiException {
        Map<String, String> params = Map.of("id", String.valueOf(fixtureId));

        return new FootballResponse(apiRequest.sendRequest("fixtures", "GET", params).getAsJsonObject().get("response").getAsJsonArray(),
                apiRequest.sendRequest("fixtures", "GET", params).getAsJsonObject().get("errors").getAsJsonArray(),
                apiRequest.sendRequest("fixtures", "GET", params).getAsJsonObject().get("results").getAsInt());
    }

    /**
     * Retrieve fixture by league and season
     *
     * @param leagueId The ID of the league to get fixtures for
     * @param season The season to get fixtures for
     */
    @Override
    public FootballResponse getFixturesByLeagueAndSeason(int leagueId, int season) throws FootballApiException {
        Map<String, String> params = Map.of("league", String.valueOf(leagueId), "season", String.valueOf(season));

        return new FootballResponse(apiRequest.sendRequest("fixtures", "GET", params).getAsJsonObject().get("response").getAsJsonArray(),
                apiRequest.sendRequest("fixtures", "GET", params).getAsJsonObject().get("errors").getAsJsonArray(),
                apiRequest.sendRequest("fixtures", "GET", params).getAsJsonObject().get("results").getAsInt());
    }
}
