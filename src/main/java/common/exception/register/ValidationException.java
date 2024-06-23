package common.exception.register;


/**
 * Exception for when a validation error occurs.
 * These errors should be handled by the frontend to begin with,
 * but if they are not, this exceptions will be thrown.
 * <b>Prevents SQL injection attacks</b>
 */
public class ValidationException extends Exception {

    public enum ValidationFailure {
        NAME,
        EMAIL,
        PASSWORD,
        DOB
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(ValidationFailure failure) {
        super("Validation failed: " + failure);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
