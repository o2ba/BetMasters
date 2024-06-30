package service.app.fixture;

import common.exception.InternalServerError;
import service.app.fixture.common.exception.FootballApiException;
import service.app.fixture.common.model.Fixture;

import java.util.List;
import java.util.Map;

public interface FixtureService {
    /**
     * Gets a fixture by its ID
     *
     * @param fixture The ID of the fixture
     * @throws FootballApiException If an error occurs while getting the fixture
     * @throws InternalServerError If an error occurs while processing the request
     */
    Fixture getFixtureByID(int fixture) throws FootballApiException, InternalServerError;

    /**
     * Gets all fixtures for a given league and season
     *
     * @param league The league ID
     * @param season The season
     * @throws FootballApiException If an error occurs while getting the fixtures
     */

    List<Fixture> getFixturesByLeagueAndSeason(int league, int season) throws FootballApiException, InternalServerError;

    /**
     * Gets the odds for a fixture
     * @param fixtureId The ID of the fixture
     * @param oddID The ID of the odd
     * @return A map of odds
     * @throws FootballApiException If an error occurs while getting the odds
     * @throws InternalServerError If an error occurs while processing the request
     */
    Map<String, Double> getOddsForFixture(int fixtureId, int oddID) throws FootballApiException, InternalServerError;


    /**
     * Gets the odds for a league and season
     *
     * @param leagueId The ID of the league
     * @param seasonId The ID of the season
     * @return A map of odds
     */
    Map<Integer, Map<String, Double>> getOddsForLeagueAndSeason(int leagueId, int seasonId, int oddID) throws FootballApiException, InternalServerError;


    /**
     * Returns a list of bet types that can be placed on a fixture
     * @return A list of bet types
     * @throws InternalServerError If an error occurs while processing the request
     */
    String getBetTypes() throws InternalServerError;


    /**
     * Gets the current season for a league ID
     *
     * @param league The league ID
     *               @return The current season
     *               @throws FootballApiException If an error occurs while getting the current season
     *               @throws InternalServerError If an error occurs while processing the request
     */
    String getCurrentSeason(int league) throws FootballApiException, InternalServerError;

}
