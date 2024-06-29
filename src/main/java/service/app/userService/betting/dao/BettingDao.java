package service.app.userService.betting.dao;

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

    /**
     * Delete a bet
     *
     * @param betID The ID of the bet
     * @throws InternalServerError If there is an error with the server
     */
    void cancelBet(int betID) throws InternalServerError;


    /**
     * Change the status of a bet
     *
     * @param betID The ID of the bet
     * @param status The new status of the bet
     * @throws InternalServerError If there is an error with the server
     */
    void changeStatus(int betID, String status) throws InternalServerError;

    /**
     * Get a bet by ID
     *
     * @param betID The ID of the bet
     * @return The bet
     * @throws InternalServerError If there is an error with the server
     */
    Map<String, Object> getBet(int betID) throws InternalServerError;

    double getBlockedAmount(int uid) throws InternalServerError;

    double getWonAmount(int uid) throws InternalServerError;

    double getLostAmount(int uid) throws InternalServerError;
}
