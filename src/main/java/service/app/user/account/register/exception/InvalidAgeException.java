package service.app.user.account.register.exception;

/**
 * Exception thrown when the age is invalid.
 * These errors should already be prevented by the front end
 * but this is a last line of defense
 */
public class InvalidAgeException extends Exception {

    /**
     * Enum for the type of age failure
     * TOO_YOUNG: The user is too young
     * INVALID: The age is invalid (e.g. negative)
     */
    public enum AgeFailureType {
        TOO_YOUNG,
        INVALID
    }

    public InvalidAgeException(AgeFailureType type) {
        super(type.toString());
    }

    public InvalidAgeException(String message) {
        super(message);
    }
}
