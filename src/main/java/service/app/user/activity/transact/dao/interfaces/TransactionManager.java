package service.app.user.activity.transact.dao.interfaces;

import common.exception.UnhandledErrorException;
import service.app.user.activity.transact.TransactionType;

import java.sql.SQLException;

/**
 * The TransactionManager interface provides a contract for managing transactions in a user's authentication.
 * Implementations of this interface are expected to provide concrete implementations of the addTransaction and addTransactionWithBet methods.
 *
 * <p>This interface is a part of the service layer in the application architecture,
 * and it interacts with the database layer to add new transactions.</p>
 *
 * <p>Exception handling is a responsibility of the implementations,
 * and they are expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
 *
 * @author o2ba
 * @version 1.0
 * @since 2024.1.2
 */
public interface TransactionManager {

    /**
     * Adds a new transaction to a user's authentication based on the user's ID, the amount, and the transaction type.
     *
     * <p>This method interacts with the database to add a new transaction.
     * It is expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
     *
     * @param uid the ID of the user who is making the transaction
     * @param amount the amount of the transaction
     * @param transactionType the type of the transaction
     * @return the ID of the newly added transaction
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     */
    int addTransaction(int uid, double amount, TransactionType transactionType)
            throws SQLException, UnhandledErrorException;

    /**
     * Adds a new transaction with a bet to a user's authentication based on the user's ID, the amount, the transaction type, and the bet ID.
     *
     * <p>This method interacts with the database to add a new transaction with a bet.
     * It is expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
     *
     * @param uid the ID of the user who is making the transaction
     * @param amount the amount of the transaction
     * @param transactionType the type of the transaction
     * @param betId the ID of the bet associated with the transaction
     * @return the ID of the newly added transaction
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     */
    int addTransactionWithBet(int uid, double amount, TransactionType transactionType, int betId)
            throws SQLException, UnhandledErrorException;
}
