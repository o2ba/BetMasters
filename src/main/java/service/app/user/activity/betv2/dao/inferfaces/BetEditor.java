package service.app.user.activity.betv2.dao.inferfaces;

import java.sql.SQLException;

public interface BetEditor {
    void changeStatus(int betId, String newStatus) throws SQLException;
}
