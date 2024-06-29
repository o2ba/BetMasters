package service.app.fixture.v2.fixture;

import common.exception.InternalServerError;
import service.app.fixture.v2.common.exception.NotFoundException;
import service.app.fixture.v2.common.model.Fixture;

import java.util.List;

public interface GetFixtureService {
    Fixture getFixtureById(int fixture) throws InternalServerError, NotFoundException;
    List<Fixture> getFixturesByLeagueAndSeason(int leagueId, int season) throws InternalServerError;
}
