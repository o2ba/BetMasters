package service.app.user.activity.transact.db.interfaces;

import common.exception.UnhandledErrorException;
import service.app.user.activity.transact.TransactionType;

import java.sql.SQLException;

public interface TransactionManager {
    int addTransaction(int uid, double amount, TransactionType transactionType) throws SQLException, UnhandledErrorException;
    int addTransactionWithBet(int uid, double amount, TransactionType transactionType, int betId) throws SQLException, UnhandledErrorException;
}
