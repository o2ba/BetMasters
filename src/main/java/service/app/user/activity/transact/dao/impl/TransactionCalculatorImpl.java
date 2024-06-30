package service.app.user.activity.transact.dao.impl;

import common.exception.UnhandledErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.transact.dao.interfaces.TransactionCalculator;
import service.general.external.dbRequest.DbRequest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
final class TransactionCalculatorImpl implements TransactionCalculator {
    private static final Logger logger = LoggerFactory.getLogger(TransactionCalculatorImpl.class);

    private static final String GET_BALANCE_QUERY =
            "SELECT SUM(amount) AS balance FROM transactions WHERE uid = ?";

    private final DbRequest dbRequest;

    @Autowired
    public TransactionCalculatorImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public double getBalance(int uid) throws SQLException, UnhandledErrorException {
        List<Map<String, Object>> result = dbRequest.query(GET_BALANCE_QUERY, uid);
        if (result.get(0).get("balance") == null) return 0;
        if (result.isEmpty()) {
            throw new UnhandledErrorException("Failed to retrieve balance");
        }
        return ((BigDecimal) result.get(0).get("balance")).doubleValue();
    }
}
