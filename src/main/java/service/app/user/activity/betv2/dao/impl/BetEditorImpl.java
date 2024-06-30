package service.app.user.activity.betv2.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.betv2.dao.inferfaces.BetEditor;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;

@Component
public class BetEditorImpl implements BetEditor {

    Logger logger = LoggerFactory.getLogger(BetEditorImpl.class);

    DbRequest dbRequest;

    @Autowired
    public BetEditorImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    private final String EDIT_STATUS = "UPDATE bets SET status = ? WHERE bet_id = ?";

    @Override
    public void changeStatus(int betId, String newStatus) throws SQLException {
        System.out.println("Changing status of bet with id: " + betId + " to: " + newStatus);
        try {
            var q = dbRequest.query(EDIT_STATUS, newStatus, betId);
        } catch (SQLException e) {
            logger.error("Error updating status: {}", e.getMessage());
            throw e;
        }
    }
}
