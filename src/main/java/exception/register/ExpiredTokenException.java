package exception.register;

/**
 * Exception for expired token. Used for email verification.
 * Tokens are limited in time and can expire.
 */
public class ExpiredTokenException extends Exception {

    public static final String MESSAGE_ID = "token.expired";

    public ExpiredTokenException() { }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
