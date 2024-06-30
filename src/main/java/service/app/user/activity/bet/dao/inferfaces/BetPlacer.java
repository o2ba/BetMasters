package service.app.user.activity.bet.dao.inferfaces;

import java.sql.SQLException;

/**
 * This interface provides a contract for placing bets.
 */
public interface BetPlacer {

    /**
     * Places a bet for a user.
     *
     * @param uid the unique identifier of the user
     * @param amount the amount of the bet
     * @param fixtureId the unique identifier of the fixture
     * @param betType the type of the bet
     * @param prediction the prediction for the bet
     * @param win_multiplier the multiplier for the win
     * @throws SQLException if a database access error occurs
     * @return the unique identifier of the placed bet
     */
    int placeBet(int uid, double amount, int fixtureId, String betType, String prediction, double win_multiplier)
            throws SQLException;
}
