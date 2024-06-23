package service.app.authRequestService.simpleBettingService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BettingServiceImpl implements BettingService {

    @Value("${bookmaker}")
    private int bookmaker;


    @Override
    public void placeBet(String jwtToken, String email, int uid, int fixtureID, String betType, String selectedBet, double amount) {

    }

    @Override
    public void claimBet(String jwtToken, String email, int uid, int matchID, int teamID) {

    }

    @Override
    public void cancelBet(String jwtToken, String email, int uid, int matchID, int teamID) {

    }

    @Override
    public void getBets(String jwtToken, String email, int uid) {

    }
}
