package service.app.userService.transaction;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.transactions.InvalidRecipientException;
import common.exception.transactions.NotEnoughBalanceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.userService.transaction.dao.TransactionDao;
import service.general.authService.AuthorizationService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    AuthorizationService authService;
    TransactionDao transactionDao;

    @Autowired
    public TransactionServiceImpl(AuthorizationService authorizationService, TransactionDao transactionDao) {
        this.authService = authorizationService;
        this.transactionDao = transactionDao;
    }


    @Override
    public void deposit(String jwtToken, String email, int uid, double amount) throws NotAuthorizedException,
            SQLException, InternalServerError {
        authService.authorizeRequest(jwtToken, uid, email);
        transactionDao.deposit(uid, amount);
    }

    /**
     * @param jwtToken
     * @param email
     * @param uid
     * @param amount
     * @throws NotAuthorizedException
     * @throws SQLException
     */
    @Override
    public void withdraw(String jwtToken, String email, int uid, double amount) throws NotAuthorizedException,
            SQLException, InternalServerError, NotEnoughBalanceException {
        authService.authorizeRequest(jwtToken, uid, email);

        // Check for sufficient balance
        double balance = transactionDao.getBalance(uid);
        if (balance < amount) {
            throw new NotEnoughBalanceException("Not enough balance");
        }
        transactionDao.withdraw(uid, amount);
    }


    @Override
    public void transfer(String jwtToken, String email, int uid, int receiverID, double amount) throws NotAuthorizedException, SQLException, InternalServerError, NotEnoughBalanceException, InvalidRecipientException {
        authService.authorizeRequest(jwtToken, uid, email);
        // Check for sufficient balance
        double balance = transactionDao.getBalance(uid);
        if (balance < amount) {
            throw new NotEnoughBalanceException("Not enough balance");
        }
        transactionDao.transfer(uid, receiverID, amount);
    }

    /**
     * To be used internally, if for example a user wins a bet
     *
     * @param uid
     * @param amount
     */
    @Override
    public void addMoneyInternal(int uid, double amount, String message) throws SQLException, InternalServerError {
        transactionDao.addMoneyInternal(uid, amount, message);
    }

    /**
     * To be used internally, if for example a user wins a bet
     *
     * @param uid
     * @param amount
     */
    @Override
    public void withdrawMoneyInternal(int uid, double amount, String message) throws SQLException, InternalServerError, NotEnoughBalanceException {
        // Check for sufficient balance
        double balance = transactionDao.getBalance(uid);
        if (balance < amount) {
            throw new NotEnoughBalanceException("Not enough balance");
        }
        transactionDao.withdrawMoneyInternal(uid, amount, message);
    }

    @Override
    public JSONArray getTransactions(String jwtToken, String email, int uid) throws NotAuthorizedException, SQLException, InternalServerError {
        authService.authorizeRequest(jwtToken, uid, email);
        List<Map<String, Object>> transactions = transactionDao.getTransactions(uid);

        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> transaction : transactions) {
            JSONObject jsonObject = new JSONObject(transaction);
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public double getBalance(String jwtToken, String email, int uid) throws NotAuthorizedException, SQLException,
            InternalServerError {
        authService.authorizeRequest(jwtToken, uid, email);
        return transactionDao.getBalance(uid);
    }
}
