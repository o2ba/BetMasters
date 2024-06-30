package service.app.user.activity.bet.helper;

import service.app.user.activity.bet.exception.InvalidInputException;
import service.app.user.activity.bet.exception.NoOddsForGameException;

/**
 * The BetHelper interface provides a contract for validating bets in the betting system.
 * Implementations of this interface are expected to provide a concrete implementation of the getOddsMultiplier method.
 *
 * <p>This interface is a part of the service layer in the application architecture,
 * and it interacts with the database layer to retrieve odds for a given bet.</p>
 *
 * <p>Exception handling is a responsibility of the implementations,
 * and they are expected to handle exceptions related to the betting not being open for the fixture or no odds being available for the game.</p>
 *
 */
public interface BetHelper {

    /**
     * Retrieves the odds multiplier for a given bet based on the fixture ID, the bet type, and the prediction.
     *
     * <p>This method interacts with the FixtureService to retrieve the odds for a given fixture and bet type.
     * It then checks if the odds are available and if the prediction is valid.</p>
     *
     * @param fixtureId the ID of the fixture the bet is placed on
     * @param betType the type of the bet (e.g. "WIN")
     * @param prediction the prediction of the bet (e.g. "Home")
     * @return the odds multiplier for the given bet
     * @throws NoOddsForGameException if there are no odds available for the game
     * @throws InvalidInputException if the prediction is invalid
     */
    double getOddsMultiplier(int fixtureId, String betType, String prediction)
            throws NoOddsForGameException, InvalidInputException;
}
