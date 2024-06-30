package service.app.fixture.fixture;

import common.exception.InternalServerError;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.fixture.common.model.Fixture;

import java.util.List;

public interface GetFixtureService {
    Fixture getFixtureById(int fixture) throws InternalServerError, FixtureNotFoundException;
    List<Fixture> getFixturesByLeagueAndSeason(int leagueId, int season) throws InternalServerError;
}
