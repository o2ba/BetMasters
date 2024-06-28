package service.app.authRequestService.simpleBettingService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.ValidationException;
import common.exception.transactions.NotEnoughBalanceException;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.app.authRequestService.authService.AuthorizationService;
import service.app.authRequestService.simpleBettingService.dao.BettingDao;
import service.app.authRequestService.transactionService.TransactionService;
import service.app.fixtureService.v2.FixtureService;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class BettingServiceImpl implements BettingService {

    @Value("${bookmaker}")
    private int bookmaker;

    BettingDao bettingDao;
    TransactionService transactionService;
    FixtureService fixtureService;
    AuthorizationService authService;

    @Autowired
    public BettingServiceImpl(AuthorizationService authService, BettingDao bettingDao, TransactionService transactionService, FixtureService fixtureService) {
        this.authService = authService;
        this.bettingDao = bettingDao;
        this.transactionService = transactionService;
        this.fixtureService = fixtureService;
    }

    @Override
    public void placeBet(String jwtToken, String email, int uid, int fixtureID, String betType, String selectedBet, double amount)
    throws InternalServerError, NotEnoughBalanceException, NotAuthorizedException, ValidationException
    {

        // authService.authorizeRequest(jwtToken, uid, email);

        if (amount <= 0) {
            throw new ValidationException("Amount must be greater than 0");
        }

        try {
            transactionService.withdrawMoneyInternal(uid, amount);
        } catch (SQLException e) {
            System.out.println("Unable to withdraw money");
            throw new InternalServerError("Internal Server Error");
        }

        bettingDao.placeBet(fixtureID, uid, new BigDecimal(amount), betType, selectedBet, new BigDecimal("1.5"));

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

