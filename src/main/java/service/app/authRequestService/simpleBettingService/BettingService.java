package service.app.authRequestService.simpleBettingService;

public interface BettingService {
    void placeBet(String jwtToken, String email, int uid,  int fixtureID, String betType, String selectedBet, double amount);
    void claimBet(String jwtToken, String email, int uid, int matchID, int teamID);
    void cancelBet(String jwtToken, String email, int uid, int matchID, int teamID);
    void getBets(String jwtToken, String email, int uid);
}
