package service.app.user.account.login;

import common.exception.UnhandledErrorException;
import service.app.user.account.login.exception.LoginDeniedException;
import common.security.SensitiveData;

/**
 * A service interface for handling user login operations.
 * Implementations of this interface provide methods to authenticate users
 * based on their username and sensitive password data.
 */
public interface LoginService {

    /** A record containing necessary login information upon successful authentication */
    record LoginPayload(String jwtToken, int uid, String email) { }

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username the username of the user attempting to log in
     * @param password the sensitive password data of the user
     * @return a {@link LoginPayload} containing necessary login information upon successful authentication
     * @throws LoginDeniedException   if the login attempt is denied due to invalid credentials, authentication status, or other policies
     * @throws UnhandledErrorException if an unexpected error occurs during the login process
     */
    LoginPayload login(String username, SensitiveData password)
            throws LoginDeniedException, UnhandledErrorException;
}
