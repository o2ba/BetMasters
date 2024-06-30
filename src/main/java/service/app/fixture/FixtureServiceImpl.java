package service.app.fixture;

import common.exception.InternalServerError;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.fixture.common.exception.FootballApiException;
import service.app.fixture.common.model.Fixture;
import service.app.fixture.fixture.GetFixtureService;
import service.app.fixture.leagues.GetLeaguesService;
import service.app.fixture.odds.GetBetTypesService;
import service.app.fixture.odds.GetOddsService;


import java.util.List;
import java.util.Map;

@Service
public class FixtureServiceImpl implements FixtureService {

    GetOddsService getOddsService;
    GetLeaguesService getLeaguesService;
    GetFixtureService getFixtureService;
    GetBetTypesService getBetTypesService;

    @Autowired
    public FixtureServiceImpl(GetOddsService getOddsService, GetLeaguesService getLeaguesService, GetFixtureService getFixtureService, GetBetTypesService getBetTypesService) {
        this.getOddsService = getOddsService;
        this.getLeaguesService = getLeaguesService;
        this.getFixtureService = getFixtureService;
        this.getBetTypesService = getBetTypesService;
    }

    /**
     * Gets a fixture by its ID
     *
     * @param fixture The ID of the fixture
     * @throws FootballApiException If an error occurs while getting the fixture
     * @throws InternalServerError  If an error occurs while processing the request
     */
    @Override
    public Fixture getFixtureByID(int fixture) throws FootballApiException, InternalServerError {
        try {
            return getFixtureService.getFixtureById(fixture);
        } catch (Exception e) {
            throw new InternalServerError("Error while getting fixture" + e.getMessage());
        }
    }

    /**
     * Gets all fixtures for a given league and season
     *
     * @param league The league ID
     * @param season The season
     * @throws FootballApiException If an error occurs while getting the fixtures
     */
    @Override
    public List<Fixture> getFixturesByLeagueAndSeason(int league, int season) throws FootballApiException, InternalServerError {
        try {
            return getFixtureService.getFixturesByLeagueAndSeason(league, season);
        } catch (Exception e) {
            throw new InternalServerError("Error while getting fixtures");
        }
    }

    /**
     * Gets the odds for a fixture
     *
     * @param fixtureId The ID of the fixture
     * @param oddID     The ID of the odd
     * @return A map of odds
     * @throws FootballApiException If an error occurs while getting the odds
     * @throws InternalServerError  If an error occurs while processing the request
     */
    @Override
    public Map<String, Double> getOddsForFixture(int fixtureId, int oddID) throws FootballApiException, InternalServerError {
        try {
            return getOddsService.getOdds(fixtureId, oddID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the odds for a league and season
     *
     * @param leagueId The ID of the league
     * @param seasonId The ID of the season
     * @return A map of odds
     */
    @Override
    public Map<Integer, Map<String, Double>> getOddsForLeagueAndSeason(int leagueId, int seasonId, int oddID) throws FootballApiException, InternalServerError {
        try {
            return getOddsService.getOddsForLeagueAndSeason(leagueId, seasonId, oddID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a list of bet types that can be placed on a fixture
     *
     * @return A list of bet types
     * @throws InternalServerError If an error occurs while processing the request
     */
    @Override
    public String getBetTypes() throws InternalServerError {
        try {
            return getBetTypesService.getBetTypesAsJsonArray();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the current season for a league ID
     *
     * @param league The league ID
     * @return The current season
     * @throws FootballApiException If an error occurs while getting the current season
     * @throws InternalServerError  If an error occurs while processing the request
     */
    @Override
    public String getCurrentSeason(int league) throws FootballApiException, InternalServerError {
        return "";
    }
}
