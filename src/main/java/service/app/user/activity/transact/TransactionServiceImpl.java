package service.app.user.activity.transact;

import com.google.gson.JsonArray;
import common.exception.UnhandledErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.user.activity.transact.dao.interfaces.TransactionCalculator;
import service.app.user.activity.transact.dao.interfaces.TransactionHistory;
import service.app.user.activity.transact.dao.interfaces.TransactionManager;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;
import service.app.user.activity.transact.exception.InvalidTransactionException;

import java.sql.SQLException;

/**
 * The TransactionServiceImpl class implements the TransactionService interface,
 * providing methods for handling various types of financial transactions for a user authentication.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    TransactionCalculator transactionCalculator;
    TransactionHistory transactionHistory;
    TransactionManager transactionManager;

    @Autowired
    public TransactionServiceImpl(TransactionCalculator transactionCalculator,
                                  TransactionHistory transactionHistory,
                                  TransactionManager transactionManager) {
        this.transactionCalculator = transactionCalculator;
        this.transactionHistory = transactionHistory;
        this.transactionManager = transactionManager;
    }

    @Override
    public int addMoney(int uid, double amount, TransactionType type)
            throws UnhandledErrorException, InvalidTransactionException, InvalidUserException {

        if (amount < 0) throw new InvalidTransactionException(InvalidTransactionException.Type.INVALID_AMOUNT);

        try {
            return transactionManager.addTransaction(uid, amount, type);
        } catch (SQLException e) {
            if (e.getSQLState() == null) {
                throw new UnhandledErrorException("Error occurred while adding money");
            } else if (e.getSQLState().equals("23514")) {
                throw new InvalidTransactionException(InvalidTransactionException.Type.INVALID_TRANSACTION_TYPE);
            } else if (e.getSQLState().equals("23503")) {
                throw new InvalidUserException("User does not exist");
            } else {
                throw new UnhandledErrorException("Error occurred while adding money");
            }

        }
    }

    @Override
    public int addMoneyLinkedToBet(int uid, double amount, TransactionType type, int betId) throws UnhandledErrorException {
        return 0;
    }

    @Override
    public int withdrawMoney(int uid, double amount, TransactionType type)
            throws UnhandledErrorException,
            NotEnoughBalanceException,
            InvalidTransactionException,
            InvalidUserException {

        if (amount < 0) throw new InvalidTransactionException(InvalidTransactionException.Type.INVALID_AMOUNT);
        if (getBalance(uid) < amount) throw new NotEnoughBalanceException("Insufficient balance");

        try {
            return transactionManager.addTransaction(uid, -amount, type);
        } catch (SQLException e) {
            if (e.getSQLState() == null) {
                throw new UnhandledErrorException("Error occurred while withdrawing money");
            } else if (e.getSQLState().equals("23514")) {
                throw new InvalidTransactionException(InvalidTransactionException.Type.INVALID_TRANSACTION_TYPE);
            } else if (e.getSQLState().equals("23503")) {
                throw new InvalidUserException("User does not exist");
            } else {
                throw new UnhandledErrorException("Error occurred while withdrawing money");
            }
        }

    }

    @Override
    public int removeMoneyLinkedToBet(int uid, double amount, String message, int betId) throws UnhandledErrorException {
        return 0;
    }

    @Override
    public JsonArray getTransactions(int uid) throws UnhandledErrorException {
        try {
            return transactionHistory.getTransactions(uid);
        } catch (SQLException e) {
            throw new UnhandledErrorException("Error occurred while retrieving transactions");
        }
    }

    @Override
    public double getBalance(int uid) throws UnhandledErrorException, InvalidUserException {
        try {
            return transactionCalculator.getBalance(uid);
        } catch (SQLException e) {
            if (e.getSQLState() == null) {
                throw new UnhandledErrorException("Error occurred while retrieving balance");
            } else if (e.getSQLState().equals("23503")) {
                throw new InvalidUserException("User does not exist");
            } else {
                throw new UnhandledErrorException("Error occurred while retrieving balance");
            }
        }
    }
}