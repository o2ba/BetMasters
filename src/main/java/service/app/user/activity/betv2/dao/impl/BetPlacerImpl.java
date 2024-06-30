package service.app.user.activity.betv2.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.betv2.dao.inferfaces.BetPlacer;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class BetPlacerImpl implements BetPlacer {

    private static final String INSERT_BET =
            "INSERT INTO bets (uid, fixture_id, bet_amount, bet_type, selected_bet, win_multiplier, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'pending')" +
                    "RETURNING bet_id";

    Logger logger = LoggerFactory.getLogger(BetPlacerImpl.class);

    DbRequest dbRequest;

    @Autowired
    public BetPlacerImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public int placeBet(int uid, double amount, int fixtureId, String betType, String prediction, double win_multiplier) throws SQLException {
        try {
             List<Map<String, Object>> q = dbRequest.query(
                    INSERT_BET,
                    uid, fixtureId, amount, betType, prediction, win_multiplier);

            if (q != null && q.size() > 0) {
                return (int) q.get(0).get("bet_id");
            } else {
                logger.error("Could not place bet: No bet_id returned");
                throw new SQLException("Error occurred while placing bet");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while placing bet", e);
            throw e;
        }
    }
}
