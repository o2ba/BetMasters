package service.app.user.activity.betv2;

import com.google.gson.GsonBuilder;
import common.exception.InternalServerError;
import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.fixture.FixtureService;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.fixture.common.model.Fixture;
import service.app.user.activity.betv2.dao.inferfaces.BetEditor;
import service.app.user.activity.betv2.dao.inferfaces.BetPlacer;
import service.app.user.activity.betv2.dao.inferfaces.BetRetriever;
import service.app.user.activity.betv2.exception.BettingNotOpenException;
import service.app.user.activity.betv2.exception.InvalidInputException;
import service.app.user.activity.betv2.exception.NoOddsForGameException;
import service.app.user.activity.betv2.helper.BetHelper;
import service.app.user.activity.betv2.helper.FixtureValidator;
import service.app.user.activity.transact.TransactionService;
import service.app.user.activity.transact.TransactionType;
import service.app.user.activity.transact.exception.InvalidTransactionException;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class BettingServiceV2Impl implements BettingServiceV2 {

    private final BetPlacer betPlacer;
    private final BetEditor betEditor;
    private BetRetriever betRetriever;
    private BetHelper betHelper;
    private TransactionService transactionService;
    private FixtureValidator fixtureValidator;
    private FixtureService fixtureService;

    @Autowired
    public BettingServiceV2Impl(BetPlacer betPlacer,
                                BetEditor betEditor,
                                BetRetriever betRetriever,
                                BetHelper betHelper,
                                TransactionService transactionService,
                                FixtureValidator fixtureValidator,
                                FixtureService fixtureService) {
        this.betPlacer = betPlacer;
        this.betEditor = betEditor;
        this.betRetriever = betRetriever;
        this.betHelper = betHelper;
        this.transactionService = transactionService;
        this.fixtureValidator = fixtureValidator;
        this.fixtureService = fixtureService;
    }

    @Override
    public int placeBet(int uid, double amount, int fixtureId, String betType, String prediction)
            throws UnhandledErrorException,
            BettingNotOpenException,
            NoOddsForGameException,
            InvalidUserException,
            NotEnoughBalanceException,
            FixtureNotFoundException,
            InvalidInputException {
        try {

            if (!fixtureValidator.bettingOpenForFixture(fixtureId))
                throw new BettingNotOpenException("Betting is not open for the fixture");

            double oddMultiplier = betHelper.getOddsMultiplier(fixtureId, betType, prediction);
            transactionService.withdrawMoney(uid, amount, TransactionType.BET_PLACED);
            return betPlacer.placeBet(uid, amount, fixtureId, betType, prediction, oddMultiplier);

        } catch (SQLException | InvalidTransactionException e) {
            throw new UnhandledErrorException(e);
        }
    }

    @Override
    public int claimBets(int uid) throws UnhandledErrorException, UserNotFoundException {

        List<Map<String, Object>> bets = getBets(uid);

        int betsClaimed = 0;

        for (Map<String, Object> bet : bets) {
            // continue if status is not pending
            if (!bet.get("status").equals("pending")) continue;

            try {
                int betId = (int) bet.get("bet_id");
                int fixtureId = (int) bet.get("fixture_id");
                double amount = ((BigDecimal) bet.get("bet_amount")).doubleValue();
                String betType = (String) bet.get("bet_type");
                String prediction = (String) bet.get("selected_bet");
                double oddMultiplier = ((BigDecimal) bet.get("win_multiplier")).doubleValue();

                Fixture fixture = fixtureService.getFixtureByID(fixtureId);

                if (!fixture.claimingAllowed()) continue;

                if (betType.equals("WIN")) {
                    StringBuilder message = new StringBuilder();
                    message.append("Bet won on fixture ")
                            .append(fixtureId).append(" with bet type ")
                            .append(betType).append(" and selected bet ")
                            .append(prediction);

                    switch (prediction) {
                        case "Home":
                            if (fixture.homeGoals() > fixture.awayGoals()) {
                                betsClaimed++;
                                transactionService.addMoney(uid, amount * oddMultiplier, TransactionType.INTERNAL);
                                betEditor.changeStatus(betId, "won");
                            } else {
                                betsClaimed++;
                                betEditor.changeStatus(betId, "lost");
                            }
                            break;
                        case "Away":
                            if (fixture.awayGoals() > fixture.homeGoals()) {
                                betsClaimed++;
                                transactionService.addMoney(uid, amount * oddMultiplier, TransactionType.INTERNAL);
                                betEditor.changeStatus(betId, "won");
                            } else {
                                betsClaimed++;
                                betEditor.changeStatus(betId, "lost");
                            }
                            break;
                        case "Draw":
                            if (fixture.awayGoals() == fixture.homeGoals()) {
                                betsClaimed++;
                                transactionService.addMoney(uid, amount * oddMultiplier, TransactionType.INTERNAL);
                                betEditor.changeStatus(betId, "won");
                            } else {
                                betsClaimed++;
                                betEditor.changeStatus(betId, "lost");
                            }
                            break;
                        default:
                            throw new InternalServerError("Invalid selected bet");
                    }
                }

            } catch (Exception e) {
                continue;
            }

        }

        return betsClaimed;
    }

    @Override
    public List<Map<String, Object>> getBets(int uid) throws UnhandledErrorException, UserNotFoundException {
        List<Map<String, Object>> bets;
        try {
            return betRetriever.retrieveBets(uid);
        } catch (SQLException e) {
            System.out.println(e.getSQLState());

            if (e.getSQLState().equals("23503"))
                throw new UserNotFoundException("User not found in the database");
            throw new UnhandledErrorException(e);
        }
    }
}