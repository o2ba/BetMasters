package service.app.authRequestService.simpleBettingService.dao;

import common.exception.InternalServerError;
import dto.PostgresRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    /**
     * Get all the bets of a user
     *
     * @param uid The user ID
     * @return The bets of the user
     * @throws InternalServerError If there is an error with the server
     */
    @Override
    public List<Map<String, Object>> getBets(int uid) throws InternalServerError {
        try {
            return postgresRequest.safeExecuteQuery(
                    BettingQueries.GET_BETS.getQuery(),
                    uid
            );
        } catch (Exception e) {
            throw new InternalServerError("An error occurred while processing the deposit");
        }
    }
}