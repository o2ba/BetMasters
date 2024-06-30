package service.app.user.account.register.exception;

/**
 * Exception thrown when the email is already in use
 */
public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
