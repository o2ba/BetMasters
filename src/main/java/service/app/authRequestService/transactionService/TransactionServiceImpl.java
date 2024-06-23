package service.app.authRequestService.transactionService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.authRequestService.authService.AuthorizationService;

import java.sql.SQLException;

@Service
public class TransactionServiceImpl implements TransactionService {

    AuthorizationService authService;

    @Autowired
    public TransactionServiceImpl(AuthorizationService authorizationService) {
        this.authService = authorizationService;
    }


    @Override
    public void deposit(String jwtToken, String email, int uid, double amount) throws NotAuthorizedException,
            SQLException, InternalServerError {
        authService.authorizeRequest(jwtToken, uid, email);
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
            SQLException, InternalServerError {
        authService.authorizeRequest(jwtToken, uid, email);

    }


    @Override
    public void transfer(String jwtToken, String email, int uid, int receiverID, double amount) throws NotAuthorizedException, SQLException, InternalServerError {
        authService.authorizeRequest(jwtToken, uid, email);

    }

    @Override
    public void getTransactions(String jwtToken, String email, int uid) throws NotAuthorizedException, SQLException, InternalServerError {

    }

    @Override
    public void getBalance(String jwtToken, String email, int uid) throws NotAuthorizedException, SQLException, InternalServerError {

    }

    /**
     * To be used internally, if for example a user wins a bet
     *
     * @param uid
     * @param amount
     */
    @Override
    public void addMoneyInternal(int uid, double amount) throws SQLException, InternalServerError {

    }

    /**
     * To be used internally, if for example a user wins a bet
     *
     * @param uid
     * @param amount
     */
    @Override
    public void withdrawMoneyInternal(int uid, double amount) throws SQLException, InternalServerError {

    }
}
