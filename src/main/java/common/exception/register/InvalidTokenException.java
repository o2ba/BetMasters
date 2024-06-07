package common.exception.register;


/**
 * Exception for invalid token. Used for email verification.
 * An example of an invalid token is when the token is not found in the database.
 */
public class InvalidTokenException extends Exception {

    public static final String messageId = "token.invalid";

    public InvalidTokenException() {
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
