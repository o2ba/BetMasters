package service.app.user.account.login.dao;

import common.exception.UnhandledErrorException;
import common.security.EncryptedData;
import service.app.user.account.login.exception.LoginDeniedException;

/**
 * An interface for accessing user login information from a database.
 * Implementations of this interface provide methods to verify if the provided
 * email address and sensitive password match stored credentials.
 */
public interface LoginServiceDao {

    /** A record containing the encrypted password and user ID upon successful verification */
    record LoginServiceDbReturn (EncryptedData encryptedData, int uid) {}

    /**
     * Verifies if the provided email address and sensitive password match stored credentials.
     *
     * @param email the email address of the user
     * @return the encrypted password of the user
     * @throws UnhandledErrorException if an unexpected error occurs during the verification process
     * @throws LoginDeniedException if the login verification fails due to reasons such as email not found or incorrect password
     */
    LoginServiceDbReturn getEncryptedPassword(String email)
            throws UnhandledErrorException, LoginDeniedException;

}
