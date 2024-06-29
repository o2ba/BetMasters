package service.app.user.activity.transact.db.interfaces;

import java.sql.SQLException;

public interface TransactionCalculator {
    double getBalance(int uid) throws SQLException;
}
