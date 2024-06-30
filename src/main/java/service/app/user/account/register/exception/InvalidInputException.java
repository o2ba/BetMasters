package service.app.user.account.register.exception;

/**
 * Exception thrown when the input is invalid
 * These errors should already be prevented by the front end
 * but this is a last line of defense
 */
public class InvalidInputException extends Exception {

    public enum ValidationFailureType {
        NAME,
        EMAIL,
        PASSWORD,
        DOB
    }

    public InvalidInputException(ValidationFailureType type) {
        super(type.toString());
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public ValidationFailureType getType() {
        return ValidationFailureType.valueOf(getMessage());
    }
}
