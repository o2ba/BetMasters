package exception.user;

public class InvalidTokenException extends Exception {

    public static final String messageId = "token.invalid";

    public InvalidTokenException() {
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
