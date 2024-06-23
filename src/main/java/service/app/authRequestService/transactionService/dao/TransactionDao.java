package service.app.authRequestService.transactionService.dao;

import common.exception.InternalServerError;
import common.exception.transactions.InvalidRecipientException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TransactionDao {
    /**
     * Deposits a specified amount into the account of a user.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be deposited.
     * @throws InternalServerError If an error occurs while processing the deposit.
     */
    void deposit(int uid, double amount) throws InternalServerError;

    /**
     * Withdraws a specified amount from the account of a user.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be withdrawn.
     * @throws InternalServerError If an error occurs while processing the withdrawal.
     */
    void withdraw(int uid, double amount) throws InternalServerError;

    /**
     * Transfers a specified amount from the account of a user to another account.
     *
     * @param uid    The unique identifier of the user.
     * @param recipient The unique identifier of the recipient.
     * @param amount The amount to be transferred.
     * @throws InternalServerError If an error occurs while processing the transfer.
     */
    void transfer(int uid, int recipient, double amount) throws InternalServerError, InvalidRecipientException;

    /**
     * Adds a specified amount to the account of a user internally.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be added.
     * @param message The message to be added. This could be a description of the transaction.
     * @throws InternalServerError If an error occurs while adding the money.
     */
    void addMoneyInternal(int uid, double amount, String message) throws InternalServerError;

    /**
     * Withdraws a specified amount from the account of a user internally.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be withdrawn.
     * @param message The message to be added. This could be a description of the transaction.
     * @throws InternalServerError If an error occurs while withdrawing the money.
     */
    void withdrawMoneyInternal(int uid, double amount, String message) throws InternalServerError;

    /**
     * Retrieves the transactions of a user.
     *
     * @param uid    The unique identifier of the user.
     * @throws InternalServerError If an error occurs while retrieving the transactions.
     */
    List<Map<String, Object>> getTransactions(int uid) throws InternalServerError;

    /**
     * Retrieves the balance of a user.
     *
     * @param uid    The unique identifier of the user.
     * @return The balance of the user.
     * @throws InternalServerError If an error occurs while retrieving the balance.
     */
    double getBalance(int uid) throws InternalServerError;
}
