package service.app.user.activity.transact.dao.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import common.exception.UnhandledErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.transact.dao.interfaces.TransactionHistory;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
final class TransactionHistoryImpl implements TransactionHistory {

    private final Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(TransactionHistoryImpl.class);

    private static final String GET_TRANSACTIONS_QUERY =
            "SELECT * FROM transactions WHERE uid = ? ORDER BY transaction_date DESC";

    private final DbRequest dbRequest;

    @Autowired
    public TransactionHistoryImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public JsonArray getTransactions(int uid) throws SQLException, UnhandledErrorException {
        try {
            List<Map<String, Object>> result = dbRequest.query(GET_TRANSACTIONS_QUERY, uid);
            JsonArray transactions = new JsonArray();
            for (Map<String, Object> row : result) {
                JsonObject transaction = new JsonObject();
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
        }
    }

    private void safePut(JsonObject jsonObject, String key, Object value) {
        if (value == null) {
            jsonObject.add(key, JsonNull.INSTANCE);
        } else if (value instanceof Number) {
            jsonObject.addProperty(key, (Number) value);
        } else if (value instanceof String) {
            jsonObject.addProperty(key, (String) value);
        } else if (value instanceof Boolean) {
            jsonObject.addProperty(key, (Boolean) value);
        } else if (value instanceof Character) {
            jsonObject.addProperty(key, (Character) value);
        } else {
            jsonObject.add(key, gson.toJsonTree(value));
        }
    }
}
