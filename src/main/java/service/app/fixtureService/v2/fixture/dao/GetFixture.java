package service.app.fixtureService.v2.fixture.dao;

import service.app.fixtureService.v2.common.model.FootballResponse;

public interface GetFixture {

    /**
     * Retrieve fixture by fixtureId
     * @param fixtureId The ID of the fixture to get
     * @return FootballResponse
     * @throws Exception if the request fails
     */
    FootballResponse getFixture(int fixtureId) throws Exception;

}
