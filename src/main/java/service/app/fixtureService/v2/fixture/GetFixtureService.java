package service.app.fixtureService.v2.fixture;

import common.exception.InternalServerError;
import service.app.fixtureService.v2.common.exception.NotFoundException;
import service.app.fixtureService.v2.common.model.Fixture;

import java.text.ParseException;
import java.util.List;

public interface GetFixtureService {
    Fixture getFixtureById(int fixture) throws InternalServerError, NotFoundException;
    List<Fixture> getFixturesByLeagueAndSeason(int leagueId, int season) throws InternalServerError;
}
