package service.app.fixture.v2.fixture.request;

import service.app.fixture.v2.common.exception.FootballApiException;
import service.app.fixture.v2.common.exception.NotFoundException;
import service.app.fixture.v2.common.model.FootballResponse;

public interface GetFixture {

    /**
     * Retrieve fixture by fixtureId
     * @param fixtureId The ID of the fixture to get
     * @return FootballResponse
     * @throws Exception if the request fails
     */
    FootballResponse getFixtureById(int fixtureId) throws FootballApiException, NotFoundException;


    FootballResponse getFixturesByLeagueAndSeason(int leagueId, int season) throws FootballApiException, NotFoundException;

}
