package service.app.authRequestService.simpleBettingService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.transactions.NotEnoughBalanceException;
import service.app.authRequestService.simpleBettingService.exceptions.BettingNotPossibleException;
import service.app.authRequestService.simpleBettingService.exceptions.InvalidBetException;

public interface BettingService {

    /**
     * Place a bet for the user
     * @param jwtToken The JWT token of the user placing the bet
     * @param email The email of the user placing the bet
     * @param uid The ID of the user placing the bet
     * @param fixtureID The ID of the fixture to bet on
     * @param betType The type of bet. For example, "WIN"
     * @param selectedBet The selected bet. For example, the selected team ("home", "away" or "draw")
     * @param amount The amount to bet
     * @throws BettingNotPossibleException The bet is not possible to place. For example, the fixture has already started
     * @throws InvalidBetException The bet is invalid. For example, the user has a non-existent bet type
     * @throws NotEnoughBalanceException The user does not have enough balance to place the bet
     * @throws NotAuthorizedException The user is not authorized to place the bet
     * @throws InternalServerError An error occurred in the server
     */
    void placeBet(String jwtToken, String email, int uid, int fixtureID, String betType, String selectedBet, double amount)
    throws BettingNotPossibleException, InvalidBetException, NotEnoughBalanceException, NotAuthorizedException, InternalServerError;


    /**
     * Claims all the bets that the user has won
     *
     * @param jwtToken The JWT token of the user claiming the bets
     * @param email The email of the user claiming the bets
     * @param uid The ID of the user claiming the bets
     * @throws NotAuthorizedException The user is not authorized to claim the bets
     * @throws InternalServerError An error occurred in the server
     */
    void claimBets(String jwtToken, String email, int uid)
    throws NotAuthorizedException, InternalServerError, InvalidBetException;

}