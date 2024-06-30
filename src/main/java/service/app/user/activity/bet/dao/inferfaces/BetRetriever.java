package service.app.user.activity.bet.dao.inferfaces;

import common.exception.gen.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * This interface provides a contract for retrieving bets.
 */
public interface BetRetriever {

    /**
     * Retrieves the bets for a user.
     *
     * @param uid the unique identifier of the user
     * @throws SQLException if a database access error occurs
     * @throws UserNotFoundException if no user is found with the provided ID
     * @return a list of maps, where each map represents a bet
     */
    List<Map<String, Object>> retrieveBets(int uid) throws SQLException, UserNotFoundException;
}
