package common.exception.login;

import common.exception.gen.RateLimitException;

/**
 * Exception for when a user has attempted to log in too many times with the wrong password.
 * This is different from {@link RateLimitException} because it is specific to the login process.
 */
public class WrongPasswordRateLimitException extends Exception {

    public WrongPasswordRateLimitException() {
        super("Too many failed attempts. Please wait before trying again.");
    }
    public WrongPasswordRateLimitException(String message) {
        super(message);
    }
    public WrongPasswordRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }

}
