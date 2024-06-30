package service.app.user.activity.transact.dao.impl;

import common.exception.UnhandledErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.transact.TransactionType;
import service.app.user.activity.transact.dao.interfaces.TransactionManager;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
final class TransactionManagerImpl implements TransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(TransactionManagerImpl.class);

    private static final String ADD_TRANSACTION_QUERY =
            "INSERT INTO transactions (uid, amount, transaction_type, transaction_date) " +
                    "VALUES (?, ?, (SELECT id FROM transaction_types WHERE type = ?), CURRENT_TIMESTAMP) " +
                    "RETURNING transaction_id";

    private static final String ADD_TRANSACTION_WITH_BET_QUERY =
            "INSERT INTO transactions (uid, amount, transaction_type, transaction_date, bet_id) " +
                    "VALUES (?, ?, (SELECT id FROM transaction_types WHERE type = ?), CURRENT_TIMESTAMP, ?) " +
                    "RETURNING transaction_id";

    private final DbRequest dbRequest;

    @Autowired
    public TransactionManagerImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public int addTransaction(int uid, double amount, TransactionType transactionType) throws SQLException, UnhandledErrorException {
        return handleTransaction(ADD_TRANSACTION_QUERY, uid, amount, transactionType, null);
    }

    @Override
    public int addTransactionWithBet(int uid, double amount, TransactionType transactionType, int betId) throws SQLException, UnhandledErrorException {
        return handleTransaction(ADD_TRANSACTION_WITH_BET_QUERY, uid, amount, transactionType, betId);
    }

    private int handleTransaction(String query, int uid, double amount, TransactionType transactionType, Integer betId) throws SQLException, UnhandledErrorException {
        List<Map<String, Object>> result;
        if (betId == null) {
            result = dbRequest.query(query, uid, amount, transactionType.getType());
        } else {
            result = dbRequest.query(query, uid, amount, transactionType.getType(), betId);
        }
        if (result.isEmpty()) {
            throw new UnhandledErrorException("Failed to retrieve transaction ID");
        }
        return (int) result.get(0).get("transaction_id");
    }
}
