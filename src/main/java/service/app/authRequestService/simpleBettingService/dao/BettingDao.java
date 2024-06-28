package service.app.authRequestService.simpleBettingService.dao;

import common.exception.InternalServerError;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BettingDao {

    /**
     * Place a bet on a fixture
     * @param fixtureID The fixture ID
     * @param uid The user ID
     * @param betType The type of bet
     * @param odds The odds of the bet. This is the amount the user will win if the bet is successful
     * @return The ID of the bet
     * @throws InternalServerError If there is an error with the server
     */
    int placeBet(int fixtureID,
                  int uid,
                  BigDecimal amount,
                  String betType,
                  String selectedBet,
                  BigDecimal odds
                  )
    throws InternalServerError;

    /**
     * Get all the bets of a user
     *
     * @param uid The user ID
     * @return The bets of the user
     * @throws InternalServerError If there is an error with the server
     */
    List<Map<String, Object>> getBets(int uid) throws InternalServerError;
}
