package service.app.authRequestService.transactionService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;

import java.sql.SQLException;

public interface TransactionService {
    void deposit(String jwtToken, String email, int uid, double amount)
    throws NotAuthorizedException, SQLException, InternalServerError;

    void withdraw(String jwtToken, String email, int uid, double amount)
    throws NotAuthorizedException, SQLException, InternalServerError;

    void transfer(String jwtToken, String email, int uid, int receiverID, double amount)
    throws NotAuthorizedException, SQLException, InternalServerError;

    void getTransactions(String jwtToken, String email, int uid)
    throws NotAuthorizedException, SQLException, InternalServerError;

    void getBalance(String jwtToken, String email, int uid)
    throws NotAuthorizedException, SQLException, InternalServerError;

    void addMoneyInternal(int uid, double amount)
    throws SQLException, InternalServerError;

    void withdrawMoneyInternal(int uid, double amount)
    throws SQLException, InternalServerError;
}
