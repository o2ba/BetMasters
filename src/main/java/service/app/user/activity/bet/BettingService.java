package service.app.user.activity.bet;

import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;
import service.app.user.activity.bet.exception.BettingNotOpenException;
import service.app.user.activity.bet.exception.InvalidInputException;
import service.app.user.activity.bet.exception.NoOddsForGameException;
import service.app.user.activity.bet.exception.StatusAlreadyIdentical;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;
import service.app.fixture.common.exception.FixtureNotFoundException;

import java.util.List;
import java.util.Map;

public interface BettingService {

    /**
     * Places a new bet for a user based on the user's ID, the amount, the fixture ID, the bet type, and the prediction.
     *
     * <p>This method interacts with the database to place a new bet.
     * It is expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
     *
     * @param uid the ID of the user who is placing the bet
     * @param amount the amount of the bet
     * @param fixtureId the ID of the fixture the bet is placed on
     * @param betType the type of the bet (e.g. "WIN")
     * @param prediction the prediction of the bet (e.g. "Home")
     * @return the ID of the newly placed bet if the bet is successfully placed
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     * @throws BettingNotOpenException if the betting is not open for the fixture
     * @throws NoOddsForGameException if there are no odds available for the game
     */
    int placeBet(int uid, double amount, int fixtureId, String betType, String prediction)
        throws UnhandledErrorException,
                BettingNotOpenException,
                NoOddsForGameException,
                NotEnoughBalanceException,
                FixtureNotFoundException,
                UserNotFoundException, InvalidInputException, InvalidUserException;


    /**
     * Claims the bets for a user based on the user's ID.
     * @param uid the ID of the user who is claiming the bets
     * @return the number of bets claimed
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     * @throws UserNotFoundException if the user is not found in the database
     */
    int claimBets(int uid)
            throws UnhandledErrorException,
            UserNotFoundException;


    /**
     * Get bet details for a user based on the user's ID.
     *
     * @param uid the ID of the user who is getting the bet details
     * @return a JSON element containing the bet details
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     * @throws UserNotFoundException   if the user is not found in the database
     */
    List<Map<String, Object>> getBets(int uid)
            throws UnhandledErrorException,
            UserNotFoundException;


    /**
     * Cancels a bet for a user based on the user's ID and the bet ID.
     *
     * @param betId the ID of the bet to be cancelled
     */
    void cancelBet(int betId)
            throws UnhandledErrorException,
            UserNotFoundException, InvalidInputException, StatusAlreadyIdentical;
}
