package service.app.user.activity.betv2.dao.inferfaces;

import java.sql.SQLException;

public interface BetPlacer {
    int placeBet(int uid, double amount, int fixtureId, String betType, String prediction, double win_multiplier)
            throws SQLException;
}
