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

            var r = postgresRequest.safeExecuteQuery(
                    BettingQueries.PLACE.getQuery(),
                    uid, fixtureID, amount, betType, selectedBet, odds
            );

            return (int) r.get(0).get("bet_id");
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
                    BettingQueries.GET_BETS_FOR_USER.getQuery(),
                    uid
            );
        } catch (Exception e) {
            throw new InternalServerError("An error occurred while processing the deposit");
        }
    }

    /**
     * Cancels a bet. Changes the status of the bet to "cancelled"
     *
     * @param betID The ID of the bet
     * @throws InternalServerError If there is an error with the server
     */
    @Override
    public void cancelBet(int betID) throws InternalServerError {
       changeStatus(betID, "cancelled");
    }

    /**
     * Change the status of a bet
     *
     * @param betID  The ID of the bet
     * @param status The new status of the bet
     * @throws InternalServerError If there is an error with the server
     */
    @Override
    public void changeStatus(int betID, String status) throws InternalServerError {
        try {
            postgresRequest.executeUpdate(
                    BettingQueries.CHANGE_STATUS.getQuery(),
                    status, betID
            );
        } catch (Exception e) {
            throw new InternalServerError("An error occurred while processing the deposit");
        }
    }

    /**
     * Get a bet by its ID
     *
     * @param betID The ID of the bet
     * @return The bet
     * @throws InternalServerError If there is an error with the server
     */
    @Override
    public Map<String, Object> getBet(int betID) throws InternalServerError {
        try {
            return postgresRequest.safeExecuteQuery(
                    BettingQueries.GET_BET_BY_ID.getQuery(),
                    betID
            ).get(0);
        } catch (Exception e) {
            throw new InternalServerError("An error occurred while processing the deposit");
        }
    }

    @Override
    public double getBlockedAmount(int uid) throws InternalServerError {
        try {
            List<Map<String, Object>> result = postgresRequest.safeExecuteQuery(
                    BettingQueries.GET_TOTAL_AMOUNT_PENDING.getQuery(),
                    uid
            );

            if (result == null || result.isEmpty() || result.get(0).get("sum") == null) {
                return 0.0;
            } else {
                BigDecimal sum = (BigDecimal) result.get(0).get("sum");
                return sum == null ? 0.0 : sum.doubleValue();
            }
        } catch (Exception e) {
            throw new InternalServerError("SQL error / BLOCKED AMOUNT");
        }
    }

    @Override
    public double getWonAmount(int uid) throws InternalServerError {
        try {
            List<Map<String, Object>> result = postgresRequest.safeExecuteQuery(
                    BettingQueries.GET_TOTAL_AMOUNT_WON.getQuery(),
                    uid
            );

            System.out.println(result);
            System.out.println(result.get(0).get("sum"));

            if (result == null || result.isEmpty() || result.get(0).get("sum") == null) {
                return 0.0;
            } else {
                BigDecimal sum = (BigDecimal) result.get(0).get("sum");
                return sum == null ? 0.0 : sum.doubleValue();
            }
        } catch (Exception e) {
            throw new InternalServerError("SQL error / WINNING AMOUNT");
        }
    }

    @Override
    public double getLostAmount(int uid) throws InternalServerError {
        try {
            List<Map<String, Object>> result = postgresRequest.safeExecuteQuery(
                    BettingQueries.GET_TOTAL_AMOUNT_LOST.getQuery(),
                    uid
            );

            if (result == null || result.isEmpty() || result.get(0).get("sum") == null) {
                return 0.0;
            } else {
                BigDecimal sum = (BigDecimal) result.get(0).get("sum");
                return sum == null ? 0.0 : sum.doubleValue();
            }
        } catch (Exception e) {
            throw new InternalServerError("SQL error / LOST AMOUNT");
        }
    }

}