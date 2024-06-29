package service.app.user.activity.transact.db.interfaces;

import common.exception.UnhandledErrorException;
import net.minidev.json.JSONArray;

import java.sql.SQLException;

public interface TransactionHistory {
    JSONArray getTransactions(int uid) throws SQLException, UnhandledErrorException;
}
