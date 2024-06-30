package service.app.user.activity.transact.dao.interfaces;

import common.exception.UnhandledErrorException;

import java.sql.SQLException;

/**
 * The TransactionCalculator interface provides a contract for calculating the balance of a user's authentication.
 * Implementations of this interface are expected to provide a concrete implementation of the getBalance method.
 *
 * <p>This interface is a part of the service layer in the application architecture,
 * and it interacts with the database layer to fetch the necessary data for its calculations.</p>
 *
 * <p>Exception handling is a responsibility of the implementations,
 * and they are expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
 *
 * @author o2ba
 * @version 1.0
 * @since 2024.1.2
 */
public interface TransactionCalculator {

    /**
     * Calculates and returns the balance of a user's authentication based on the user's ID.
     *
     * <p>This method interacts with the database to fetch the necessary data for its calculations.
     * It is expected to handle SQL exceptions and other unhandled exceptions that might occur during the execution.</p>
     *
     * @param uid the ID of the user whose balance is to be calculated
     * @return the balance of the user's authentication
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws UnhandledErrorException if an unexpected error occurs during the execution of the method
     */
    double getBalance(int uid) throws SQLException, UnhandledErrorException;
}
