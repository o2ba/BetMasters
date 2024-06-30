package service.app.user.activity.bet.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.bet.dao.inferfaces.BetEditor;
import service.app.user.activity.bet.exception.StatusAlreadyIdentical;
import service.general.external.dbRequest.DbRequest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class BetEditorImpl implements BetEditor {

    Logger logger = LoggerFactory.getLogger(BetEditorImpl.class);

    DbRequest dbRequest;

    @Autowired
    public BetEditorImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    private final String GET_BET_STATUS = "SELECT status FROM bets WHERE bet_id = ?";
    private final String EDIT_STATUS = "UPDATE bets SET status = ? WHERE bet_id = ? RETURNING uid, bet_amount";

    @Override
    public BetEditorOutput changeStatus(int betId, String newStatus) throws SQLException, StatusAlreadyIdentical {
        try {
            List<Map<String, Object>> p = dbRequest.query(GET_BET_STATUS, betId);
            String current_status = (String) p.get(0).get("status");

            if (current_status.equals(newStatus)) {
                throw new StatusAlreadyIdentical("Status is already " + newStatus);
            }

            List<Map<String, Object>> q = dbRequest.query(EDIT_STATUS, newStatus, betId);
            Map<String, Object> row = q.get(0);
            return new BetEditorOutput((int) row.get("uid"), ((BigDecimal) row.get("bet_amount")).doubleValue());
        } catch (SQLException e) {
            logger.error("Error updating status: {}", e.getMessage());
            throw e;
        }
    }
}
