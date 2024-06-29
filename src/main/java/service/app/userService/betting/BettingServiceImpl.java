package service.app.userService.betting;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.transactions.NotEnoughBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.app.userService.betting.dao.BettingDao;
import service.app.userService.betting.exceptions.BettingNotPossibleException;
import service.general.authService.AuthorizationService;
import service.app.userService.betting.exceptions.BetTypeMismatchException;
import service.app.userService.betting.exceptions.InvalidBetException;
import service.app.userService.transaction.TransactionService;
import service.app.fixtureService.v2.FixtureService;
import service.app.fixtureService.v2.common.model.Fixture;
import service.app.fixtureService.v2.odds.BetTypes;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public int placeBet(String jwtToken, String email, int uid, int fixtureID, String betType, String selectedBet, double amount)
    throws BettingNotPossibleException, InvalidBetException, NotEnoughBalanceException, NotAuthorizedException, InternalServerError {

        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email);


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

        StringBuilder message = new StringBuilder();
        message.append("Bet placed on fixture ")
                .append(fixtureID).append(" with bet type ")
                .append(betType).append(" and selected bet ")
                .append(selectedBet);

        // Withdraw money from user
        try {
            transactionService.withdrawMoneyInternal(uid, amount, message.toString());
        } catch (SQLException e) {
            throw new InternalServerError("Internal Server Error. Unable to withdraw");
        }

        // Place the bet
        return bettingDao.placeBet(fixtureID, uid, new BigDecimal(amount), betType, selectedBet, new BigDecimal(oddMultiplier));

    }

    @Override
    public void claimBets(String jwtToken, String email, int uid) throws NotAuthorizedException, InternalServerError {
        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email);

        try {
            List<Map<String, Object>> bets = bettingDao.getBets(uid);

            for (var bet : bets) {

                if(!Objects.equals((String) bet.get("status"), "PENDING")) break;

                Integer betID;
                Integer fixtureID;
                String betType;
                String selectedBet;
                double betAmount;
                double oddMultiplier;

                try {
                    betID = (Integer) bet.get("bet_id");
                    fixtureID = (Integer) bet.get("fixtureid");
                    betType = (String) bet.get("bet_type");
                    selectedBet = (String) bet.get("selected_bet");
                    betAmount = ((BigDecimal) bet.get("bet_amount")).doubleValue();
                    oddMultiplier = ((BigDecimal) bet.get("odds")).doubleValue();
                } catch (Exception e) {
                    throw new InternalServerError("Extracting bet data failed");
                }

                Fixture fixture = fixtureService.getFixtureByID(fixtureID);

                if (!Objects.equals(fixture.status(), "FT")) break;

                if (betType.equals("WIN")) {

                    StringBuilder message = new StringBuilder();
                    message.append("Bet won on fixture ")
                            .append(fixtureID).append(" with bet type ")
                            .append(betType).append(" and selected bet ")
                            .append(selectedBet);

                    switch (selectedBet) {
                        case "Home":
                            if (fixture.homeGoals() > fixture.awayGoals()) {
                                transactionService.addMoneyInternal(uid, betAmount * oddMultiplier, message.toString());
                                bettingDao.changeStatus(betID, "WON");
                            } else {
                                bettingDao.changeStatus(betID, "LOST");
                            }
                            break;
                        case "Away":
                            if (fixture.awayGoals() > fixture.homeGoals()) {
                                transactionService.addMoneyInternal(uid, betAmount * oddMultiplier, message.toString());
                                bettingDao.changeStatus(betID, "WON");
                            } else {
                                bettingDao.changeStatus(betID, "LOST");
                            }
                            break;
                        case "Draw":
                            if (fixture.awayGoals() == fixture.homeGoals()) {
                                transactionService.addMoneyInternal(uid, betAmount * oddMultiplier, message.toString());
                                bettingDao.changeStatus(betID, "WON");
                            } else {
                                bettingDao.changeStatus(betID, "LOST");
                            }
                            break;
                        default:
                            throw new InternalServerError("Invalid selected bet");
                    }

                }

            }
        } catch (Exception e) {
            throw new InternalServerError("Internal Server Error. Unable to claim bets " + e.getMessage());
        }
    }

    @Override
    public void cancelBet(String jwtToken, String email, int uid, int betID) throws InternalServerError, NotAuthorizedException, BetTypeMismatchException {
        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email)

        Map<String, Object> bet = bettingDao.getBet(betID);

        if (bet == null) {
            throw new InternalServerError("Bet not found");
        }

        if(!Objects.equals((String) bet.get("status"), "PENDING")) {
            throw new BetTypeMismatchException("Bet is not pending");
        }

        try {
            bettingDao.cancelBet(betID);
            transactionService.addMoneyInternal(uid, ((BigDecimal) bet.get("bet_amount")).doubleValue(), "Bet cancelled");
        } catch (InternalServerError e) {
            throw new InternalServerError("Internal Server Error. Unable to delete bet");
        } catch (SQLException e) {
            throw new InternalServerError("Issue with transaction");
        }
    }


    @Override
    public List<Map<String, Object>> getBets(String jwtToken, String email, int uid) throws InternalServerError, NotAuthorizedException {
        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email);

        return bettingDao.getBets(uid);
    }

    @Override
    public Map<String, Double> getStatistics(String jwtToken, String email, int uid) throws InternalServerError, NotAuthorizedException {
        // TODO authorize request
        // authService.authorizeRequest(jwtToken, uid, email);

        Map<String, Double> stats = new HashMap<>();
        System.out.println("Getting stats for " + uid);

        stats.put("blocked", bettingDao.getBlockedAmount(uid));
        System.out.println("Blocked: " + bettingDao.getBlockedAmount(uid));
        stats.put("won", bettingDao.getWonAmount(uid));
        System.out.println("Won: " + bettingDao.getWonAmount(uid));
        stats.put("lost", bettingDao.getLostAmount(uid));
        System.out.println("Lost: " + bettingDao.getLostAmount(uid));

        return stats;
    }

}