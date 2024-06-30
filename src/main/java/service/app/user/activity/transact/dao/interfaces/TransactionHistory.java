package service.app.user.activity.transact.dao.interfaces;

import com.google.gson.JsonArray;
import common.exception.UnhandledErrorException;

import java.sql.SQLException;

/**
 * The TransactionHistory interface provides a contract for fetching a user's transaction history.
 * Implementations of this interface are expected to provide a concrete implementation of the getTransactions method.
 *
 * <p>This interface is a part of the service layer in the application architecture,
 * and it interacts with the database layer to fetch the necessary data.</p>
 *
 * <p>Exception handling is a responsibility of the implementations,
 * and they are expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
 *
 * @author o2ba
 * @version 1.0
 * @since 2024.1.2
 */
public interface TransactionHistory {

    /**
     * Fetches and returns the transactions of a user's authentication based on the user's ID.
     *
     * <p>This method interacts with the database to fetch the necessary data.
     * It is expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
     *
     * @param uid the ID of the user whose transactions are to be fetched
     * @return a JsonArray containing the user's transactions
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     */
    JsonArray getTransactions(int uid) throws SQLException, UnhandledErrorException;
}
