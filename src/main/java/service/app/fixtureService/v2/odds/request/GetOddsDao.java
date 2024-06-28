package service.app.fixtureService.v2.odds.request;

import dto.httpRequest.exception.RequestSendingException;
import service.app.fixtureService.v2.common.model.FootballResponse;

/**
 * Retrieve odds for a fixture
 */
public interface GetOddsDao {
    /**
     * Retrieve odds for a fixture. Uses the default bookmaker
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID The ID of the odd to get
     * @return Map<String, Double>
     */
    FootballResponse getOdds(int fixtureId, int oddID) throws RequestSendingException;

    /**
     * Retrieve odds for a fixture
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID The ID of the odd to get
     * @param bookmaker The bookmaker to get the odds from
     * @return Map<String, Double>
     */
    FootballResponse getOdds(int fixtureId, int oddID, String bookmaker) throws RequestSendingException;


    /**
     * Retrieve odds for a league and season
     * @param leagueId The ID of the league to get the odds for
     * @param season The season to get the odds for
     * @return Map<Integer, Map<String, Double>>
     */
    FootballResponse getOddsForLeagueAndSeason(int leagueId, int season, int oddID) throws RequestSendingException;

    /**
     * Retrieve odds for a league and season
     * @param leagueId The ID of the league to get the odds for
     * @param season The season to get the odds for
     * @param bookmaker The bookmaker to get the odds from
     * @return Map<Integer, Map<String, Double>>
     */
    FootballResponse getOddsForLeagueAndSeason(int leagueId, int season, String bookmaker, int oddID) throws RequestSendingException;

}
