package exception.user;

public class ExpiredTokenException extends Exception {

    public static final String MESSAGE_ID = "token.expired";

    public ExpiredTokenException() { }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
