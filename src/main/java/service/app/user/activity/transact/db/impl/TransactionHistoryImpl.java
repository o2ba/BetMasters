package service.app.user.activity.transact.db.impl;

import common.exception.UnhandledErrorException;
import net.minidev.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.transact.db.interfaces.TransactionHistory;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
final class TransactionHistoryImpl implements TransactionHistory {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHistoryImpl.class);

    private static final String GET_TRANSACTIONS_QUERY =
            "SELECT * FROM transactions WHERE uid = ? ORDER BY transaction_date DESC";

    private final DbRequest dbRequest;

    @Autowired
    public TransactionHistoryImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public JSONArray getTransactions(int uid) throws SQLException, UnhandledErrorException {
        try {
            List<Map<String, Object>> result = dbRequest.query(GET_TRANSACTIONS_QUERY, uid);
            JSONArray transactions = new JSONArray();
            for (Map<String, Object> row : result) {
                JSONObject transaction = new JSONObject();
                safePut(transaction, "transaction_id", row.get("transaction_id"));
                safePut(transaction, "uid", row.get("uid"));
                safePut(transaction, "amount", row.get("amount"));
                safePut(transaction, "transaction_type", row.get("transaction_type"));
                safePut(transaction, "transaction_date", row.get("transaction_date"));
                safePut(transaction, "bet_id", row.get("bet_id"));
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            logger.error("Error occurred while retrieving transactions", e);
            throw e;
        } catch (JSONException e) {
            logger.error("Error occurred while creating JSON object", e);
            throw new UnhandledErrorException("Error occurred while creating JSON object");
        }
    }

    private void safePut(JSONObject jsonObject, String key, Object value) throws JSONException {
        try {
            jsonObject.put(key, value != null ? value : JSONObject.NULL);
        } catch (Exception e) {
            jsonObject.put(key, JSONObject.NULL);
        }
    }
}
