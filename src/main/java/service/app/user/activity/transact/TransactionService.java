package service.app.user.activity.transact;

import com.google.gson.JsonArray;
import common.exception.UnhandledErrorException;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;
import service.app.user.activity.transact.exception.InvalidTransactionException;

/**
 * The TransactionService interface provides methods for handling various
 * types of financial transactions for a user authentication. This includes adding money,
 * withdrawing money, transferring funds between users, and retrieving transaction history
 * and balance information.
 */
public interface TransactionService {

    /**
     * Adds a specified amount of money to a user's authentication.
     *
     * @param uid     The unique identifier of the user.
     * @param amount  The amount of money to be added.
     * @param type    The type of transaction.
     * @return The unique identifier of the transaction.
     * @throws UnhandledErrorException if an unexpected error occurs while adding money.
     */
    int addMoney(int uid, double amount, TransactionType type)
            throws UnhandledErrorException, InvalidTransactionException, InvalidUserException;

    /**
     * Adds a specified amount of money to a user's authentication, linked to a specific bet
     *
     * @param uid     The unique identifier of the user.
     * @param amount  The amount of money to be added.
     * @param type    The type of transaction.
     * @param betId   The unique identifier of the bet.
     * @return The unique identifier of the transaction.
     * @throws UnhandledErrorException if an unexpected error occurs while adding money.
     */
    int addMoneyLinkedToBet(int uid, double amount, TransactionType type, int betId)
            throws UnhandledErrorException;

    /**
     * Withdraws a specified amount of money from a user's authentication.
     *
     * @param uid     The unique identifier of the user.
     * @param amount  The amount of money to be withdrawn.
     * @param type    The type of transaction.
     * @return The unique identifier of the transaction.
     * @throws UnhandledErrorException    if an unexpected error occurs while withdrawing money.
     * @throws NotEnoughBalanceException  if the user does not have sufficient balance to complete the transaction.
     */
    int withdrawMoney(int uid, double amount, TransactionType type)
            throws UnhandledErrorException, NotEnoughBalanceException, InvalidTransactionException, InvalidUserException;

    /**
     * Removes a specified amount of money to a user's authentication, linked to a specific bet
     *
     * @param uid     The unique identifier of the user.
     * @param amount  The amount of money to be added.
     * @param message A message or note associated with the transaction.
     * @param betId   The unique identifier of the bet.
     * @return The unique identifier of the transaction.
     * @throws UnhandledErrorException if an unexpected error occurs while adding money.
     */
    int removeMoneyLinkedToBet(int uid, double amount, String message, int betId)
            throws UnhandledErrorException;

    /**
     * Retrieves the transaction history for a specified user.
     *
     * @param uid The unique identifier of the user.
     * @return A JSONArray containing the transaction history of the user.
     * @throws UnhandledErrorException if an unexpected error occurs while retrieving the transaction history.
     */
    JsonArray getTransactions(int uid)
            throws UnhandledErrorException;

    /**
     * Retrieves the current balance of a user's authentication.
     *
     * @param uid      The unique identifier of the user.
     * @return The current balance of the user's authentication.
     * @throws UnhandledErrorException if an unexpected error occurs while retrieving the balance.
     */
    double getBalance(int uid)
            throws UnhandledErrorException, InvalidUserException;
}
