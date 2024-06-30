package service.app.user.activity.bet.dao.inferfaces;

import service.app.user.activity.bet.exception.StatusAlreadyIdentical;

import java.sql.SQLException;

public interface BetEditor {

    record BetEditorOutput(int uid, double betAmount) { }

    BetEditorOutput changeStatus(int betId, String newStatus) throws SQLException, StatusAlreadyIdentical;
}
