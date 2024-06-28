package service.app.authRequestService.simpleBettingService.dao;

import common.exception.InternalServerError;
import common.exception.transactions.NotEnoughBalanceException;
import dto.PostgresRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.authRequestService.simpleBettingService.exceptions.GameAlreadyStartedOrCancelledException;
import service.app.fixtureService.v2.odds.BetTypes;

import java.math.BigDecimal;

@Component
public class BettingDaoImpl implements BettingDao {

    PostgresRequest postgresRequest;

    @Autowired
    public BettingDaoImpl(PostgresRequest postgresRequest) {
        this.postgresRequest = postgresRequest;
    }



    @Override
    public int placeBet(int fixtureID, int uid, BigDecimal amount, String betType, String selectedBet, BigDecimal odds) throws InternalServerError {
        try {

            postgresRequest.executeUpdate(
                    BettingQueries.PLACE.getQuery(),
                    uid, fixtureID, amount, betType, selectedBet, odds
            );

            return 0;
        } catch (Exception e) {
            throw new InternalServerError("An error occurred while processing the deposit");
        }
    }
}