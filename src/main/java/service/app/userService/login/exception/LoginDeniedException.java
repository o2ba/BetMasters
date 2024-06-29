package service.app.userService.login.exception;

/**
 * Exception thrown when the login is denied
 * This is thrown when the email is not found or the password is incorrect
 */
public class LoginDeniedException extends Exception {
    public LoginDeniedException(String message) {
        super(message);
    }
}
