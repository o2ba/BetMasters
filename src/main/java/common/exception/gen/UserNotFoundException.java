package common.exception.gen;

/**
 * Exception for when a user is not found in the database.
 */
public class UserNotFoundException extends Exception {

    public static final String MESSAGE_ID = "user.not.found";
    public static final String MESSAGE_ID_TOKEN = "token.user.not.found";

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
