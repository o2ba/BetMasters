package service.app.authRequestService.simpleBettingService.dao;

import common.exception.InternalServerError;
import common.exception.transactions.NotEnoughBalanceException;
import service.app.fixtureService.v2.odds.BetTypes;
import service.app.authRequestService.simpleBettingService.exceptions.GameAlreadyStartedOrCancelledException;

import java.math.BigDecimal;

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
}
