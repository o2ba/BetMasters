package service.app.authRequestService.transactionService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.transactions.InvalidRecipientException;
import common.exception.transactions.NotEnoughBalanceException;
import org.json.JSONArray;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    void deposit(String jwtToken, String email, int uid, double amount)
    throws NotAuthorizedException, SQLException, InternalServerError;

    void withdraw(String jwtToken, String email, int uid, double amount)
    throws NotAuthorizedException, SQLException, InternalServerError, NotEnoughBalanceException;

    void transfer(String jwtToken, String email, int uid, int receiverID, double amount)
    throws NotAuthorizedException, SQLException, InternalServerError, NotEnoughBalanceException, InvalidRecipientException;

    void addMoneyInternal(int uid, double amount, String message)
    throws SQLException, InternalServerError;

    void withdrawMoneyInternal(int uid, double amount, String message)
    throws SQLException, InternalServerError, NotEnoughBalanceException;

    JSONArray getTransactions(String jwtToken, String email, int uid)
    throws NotAuthorizedException, SQLException, InternalServerError;

    double getBalance(String jwtToken, String email, int uid)
    throws NotAuthorizedException, SQLException, InternalServerError;
}
