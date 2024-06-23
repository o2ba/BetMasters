package service.app.authRequestService.simpleBettingService.dao;

import common.exception.InternalServerError;
import common.exception.transactions.NotEnoughBalanceException;
import service.app.authRequestService.simpleBettingService.exceptions.GameAlreadyStartedOrCancelledException;
import service.app.fixtureService.v2.model.bet.BetTypes;

public class BettingDaoImpl implements BettingDao {

    /**
     * Place a bet on a fixture
     *
     * @param fixtureID   The fixture ID
     * @param uid         The user ID
     * @param amount
     * @param betType     The type of bet
     * @param selectedBet
     * @param odds        The odds of the bet. This is the amount the user will win if the bet is successful
     * @return The ID of the bet
     * @throws NotEnoughBalanceException              If the user does not have enough balance to place the bet
     * @throws GameAlreadyStartedOrCancelledException If the game has already started or has been cancelled
     * @throws InternalServerError                    If there is an error with the server
     */
    @Override
    public int placeBet(int fixtureID, int uid, double amount, BetTypes betType, String selectedBet, double odds) throws NotEnoughBalanceException, GameAlreadyStartedOrCancelledException, InternalServerError {
        return 0;
    }
}