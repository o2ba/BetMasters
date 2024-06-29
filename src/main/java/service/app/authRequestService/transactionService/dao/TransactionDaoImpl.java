package service.app.authRequestService.transactionService.dao;

import common.exception.InternalServerError;
import common.exception.transactions.InvalidRecipientException;
import dto.PostgresRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class TransactionDaoImpl implements TransactionDao {

    private final PostgresRequest postgresRequest;

    @Autowired
    public TransactionDaoImpl(PostgresRequest postgresRequest) {
        this.postgresRequest = postgresRequest;
    }


    /**
     * Deposits a specified amount into the account of a user.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be deposited.
     * @throws InternalServerError If an error occurs while processing the deposit.
     */
    @Override
    public void deposit(int uid, double amount) throws InternalServerError {

        try {
            if (amount < 0) {
                throw new InternalServerError("Amount must be greater than 0");
            }

            postgresRequest.executeUpdate(
                    TransactionQueries.DEPOSIT.getQuery(),
                    uid, +amount
            );
        } catch (SQLException e) {
            throw new InternalServerError("An error occurred while processing the deposit");
        }
    }

    /**
     * Withdraws a specified amount from the account of a user.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be withdrawn.
     * @throws InternalServerError If an error occurs while processing the withdrawal.
     */
    @Override
    public void withdraw(int uid, double amount) throws InternalServerError {
        try {
            if (amount < 0) {
                throw new InternalServerError("Amount must be greater than 0");
            }
            postgresRequest.executeUpdate(
                    TransactionQueries.WITHDRAW.getQuery(),
                    uid, -amount
            );
        } catch (SQLException e) {
            throw new InternalServerError("An error occurred while processing the withdrawal");
        }
    }

    /**
     * Transfers a specified amount from the account of a user to another account.
     * This method should be improved in the future as currently there is the possibility of
     * money being lost in the transfer process.
     *
     * @param uid    The unique identifier of the user.
     * @param amount The amount to be transferred.
     * @throws InternalServerError If an error occurs while processing the transfer.
     */
    @Override
    public void transfer(int uid, int recipient, double amount) throws InternalServerError, InvalidRecipientException {
        try {
            if (amount < 0) {
                throw new InternalServerError("Amount must be greater than 0");
            }

            if (uid == recipient) {
                throw new InternalServerError("You cannot transfer money to yourself");
            }

            postgresRequest.executeUpdate(
                    TransactionQueries.TRANSFER.getQuery(),
                    uid, -amount
            );

            postgresRequest.executeUpdate(
                    TransactionQueries.RECEIVE.getQuery(),
                    recipient, +amount
            );

        } catch (SQLException e) {

            if ("23503".equals(e.getSQLState())) {
                throw new InvalidRecipientException("The recipient does not exist");
            }

            throw new InternalServerError("An error occurred while processing the transfer");
        }
    }

    /**
     * Adds a specified amount to the account of a user internally.
     *
     * @param uid     The unique identifier of the user.
     * @param amount  The amount to be added.
     * @param message The message to be added. This could be a description of the transaction.
     * @throws InternalServerError If an error occurs while adding the money.
     */
    @Override
    public void addMoneyInternal(int uid, double amount, String message) throws InternalServerError {
        try {
            if (amount < 0) {
                throw new InternalServerError("Amount must be greater than 0");
            }
            postgresRequest.executeUpdate(
                    TransactionQueries.ADD_INTERNAL.getQuery(),
                    uid, amount, message
            );
        } catch (SQLException e) {
            throw new InternalServerError("An error occurred while processing the withdrawal");
        }

    }

    /**
     * Withdraws a specified amount from the account of a user internally.
     *
     * @param uid     The unique identifier of the user.
     * @param amount  The amount to be withdrawn.
     * @param message The message to be added. This could be a description of the transaction.
     * @throws InternalServerError If an error occurs while withdrawing the money.
     */
    @Override
    public void withdrawMoneyInternal(int uid, double amount, String message) throws InternalServerError {
        try {
            if (amount < 0) {
                throw new InternalServerError("Amount must be greater than 0");
            }
            postgresRequest.executeUpdate(
                    TransactionQueries.REMOVE_INTERNAL.getQuery(),
                    uid, -amount, message
            );
        } catch (SQLException e) {
            throw new InternalServerError("An error occurred while processing the withdrawal");
        }
    }

    /**
     * Retrieves the transactions of a user.
     *
     * @param uid    The unique identifier of the user.
     * @throws InternalServerError If an error occurs while retrieving the transactions.
     */
    @Override
    public List<Map<String, Object>> getTransactions(int uid) throws InternalServerError {
        try {
            return postgresRequest.safeExecuteQuery(
                    TransactionQueries.GET_TRANSACTIONS.getQuery(),
                    uid
            );
        } catch (SQLException e) {
            throw new InternalServerError("An error occurred while retrieving the transactions");
        }
    }

    /**
     * Retrieves the balance of a user.
     *
     * @param uid    The unique identifier of the user.
     * @return The balance of the user.
     * @throws InternalServerError If an error occurs while retrieving the balance.
     */
    @Override
    public double getBalance(int uid) throws InternalServerError {
        try {
            List<Map<String, Object>> result = postgresRequest.safeExecuteQuery(
                    TransactionQueries.GET_BALANCE.getQuery(),
                    uid
            );

            Object sumObject = result.get(0).get("sum");

            if (sumObject == null) {
                return 0;
            }

            double sum;
            if (sumObject instanceof BigDecimal) {
                sum = ((BigDecimal) sumObject).doubleValue();
            } else {
                sum = (double) sumObject;
            }

            return sum;
        } catch (SQLException e) {
            throw new InternalServerError("An error occurred while retrieving the balance");
        }
    }


}

