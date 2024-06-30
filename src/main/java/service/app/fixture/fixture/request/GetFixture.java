package service.app.fixture.fixture.request;

import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.fixture.common.exception.FootballApiException;
import service.app.fixture.common.model.FootballResponse;

public interface GetFixture {

    /**
     * Retrieve fixture by fixtureId
     * @param fixtureId The ID of the fixture to get
     * @return FootballResponse
     * @throws Exception if the request fails
     */
    FootballResponse getFixtureById(int fixtureId) throws FootballApiException, FixtureNotFoundException;


    FootballResponse getFixturesByLeagueAndSeason(int leagueId, int season) throws FootballApiException, FixtureNotFoundException;

}
