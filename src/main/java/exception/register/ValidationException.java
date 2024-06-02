package exception.register;


/**
 * Exception for when a validation error occurs.
 * These errors should be handled by the frontend to begin with,
 * but if they are not, this exception will be thrown.
 * <b>Prevents SQL injection attacks</b>
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
