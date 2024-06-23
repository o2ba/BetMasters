package service.app.fixtureService.v2.dto;

import dto.httpRequest.exception.RequestSendingException;

/**
 * Retrieve odds for a fixture
 */
public interface GetOdds {
    /**
     * Retrieve odds for a fixture. Uses the default bookmaker
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID The ID of the odd to get
     * @return Map<String, Double>
     */
    FootballResponse getOdds(String fixtureId, int oddID) throws RequestSendingException;

    /**
     * Retrieve odds for a fixture
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID The ID of the odd to get
     * @param bookmaker The bookmaker to get the odds from
     * @return Map<String, Double>
     */
    FootballResponse getOdds(String fixtureId, int oddID, String bookmaker) throws RequestSendingException;
}
