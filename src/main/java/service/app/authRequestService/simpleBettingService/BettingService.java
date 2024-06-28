package service.app.authRequestService.simpleBettingService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.ValidationException;
import common.exception.transactions.NotEnoughBalanceException;

public interface BettingService {
    void placeBet(String jwtToken, String email, int uid, int fixtureID, String betType, String selectedBet, double amount)
    throws InternalServerError, NotEnoughBalanceException, NotAuthorizedException, ValidationException;
    void claimBet(String jwtToken, String email, int uid, int matchID, int teamID);
    void cancelBet(String jwtToken, String email, int uid, int matchID, int teamID);
    void getBets(String jwtToken, String email, int uid);
}
