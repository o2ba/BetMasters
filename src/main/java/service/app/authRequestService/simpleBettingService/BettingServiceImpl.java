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
import service.app.authRequestService.simpleBettingService.exceptions.BettingNotPossibleException;
import service.app.authRequestService.simpleBettingService.exceptions.InvalidBetException;
import service.app.authRequestService.transactionService.TransactionService;
import service.app.fixtureService.v2.FixtureService;
import service.app.fixtureService.v2.odds.BetTypes;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

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

    /**
     * Place a bet for the user
     *
     * @param jwtToken    The JWT token of the user placing the bet
     * @param email       The email of the user placing the bet
     * @param uid         The ID of the user placing the bet
     * @param fixtureID   The ID of the fixture to bet on
     * @param betType     The type of bet. For example, "WIN"
     * @param selectedBet The selected bet. For example, the selected team ("home", "away" or "draw")
     * @param amount      The amount to bet
     * @throws BettingNotPossibleException The bet is not possible to place. For example, the fixture has already started
     * @throws InvalidBetException         The bet is invalid. For example, the user has a non-existent bet type
     * @throws NotEnoughBalanceException   The user does not have enough balance to place the bet
     * @throws NotAuthorizedException      The user is not authorized to place the bet
     * @throws InternalServerError         An error occurred in the server
     */
    @Override
    public void placeBet(String jwtToken, String email, int uid, int fixtureID, String betType, String selectedBet, double amount)
    throws BettingNotPossibleException, InvalidBetException, NotEnoughBalanceException, NotAuthorizedException, InternalServerError {

        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email);


        System.out.println("Place bet");

        // Check if bet type is valid, get betID
        BetTypes selectedBetType = null;
        for (BetTypes bet : BetTypes.values()) {
            if (bet.getShortName().equals(betType)) {
                selectedBetType = bet;
                break;
            }
        }

        if (selectedBetType == null) {
            throw new InvalidBetException("Invalid bet type");
        }

        // The multiplier of winning amount
        Double oddMultiplier;

        // Get the odds for the fixture
        try {
            var odds = fixtureService.getOddsForFixture(fixtureID, selectedBetType.getId());

            System.out.println("Odds: " + odds);
            // check if selected bet is valid
            if (!odds.containsKey(selectedBet)) {
                throw new InvalidBetException("Invalid bet. Valid bets are: " + selectedBetType.getPossibleValues());
            } else {
                oddMultiplier = odds.get(selectedBet);
            }
        } catch (Exception e) {
            throw new InternalServerError("Internal Server Error");
        }

        // Check if amount is valid

        if (amount <= 0) {
            throw new InvalidBetException("Amount must be greater than 0");
        }


        // Get the fixture
        try {
            var fixture = fixtureService.getFixtureByID(fixtureID);
            // Check if the fixture has already started
            if (fixture.bettingAllowed()) {
                throw new BettingNotPossibleException("Betting is not allowed for this fixture");
            }
        } catch (Exception e) {
            throw new InternalServerError("Internal Server Error. Unable to get fixture");
        }

        // Withdraw money from user
        try {
            transactionService.withdrawMoneyInternal(uid, amount);
        } catch (SQLException e) {
            throw new InternalServerError("Internal Server Error. Unable to withdraw");
        }

        // Place the bet
        bettingDao.placeBet(fixtureID, uid, new BigDecimal(amount), betType, selectedBet, new BigDecimal(oddMultiplier));

    }

    /**
     * Claims all the bets that the user has won
     *
     * @param jwtToken The JWT token of the user claiming the bets
     * @param email    The email of the user claiming the bets
     * @param uid      The ID of the user claiming the bets
     * @throws NotAuthorizedException The user is not authorized to claim the bets
     * @throws InternalServerError    An error occurred in the server
     */
    @Override
    public void claimBets(String jwtToken, String email, int uid) throws NotAuthorizedException, InternalServerError {
        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email);

        // Get all the bets of the user
        try {
            var bets = bettingDao.getBets(uid);

            System.out.println("Bets:" + bets);

            for (var bet : bets) {
                System.out.println(bet);
            }
        } catch (Exception e) {
            throw new InternalServerError("Internal Server Error. Unable to claim bets");
        }
    }


}

