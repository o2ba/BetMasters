package exception.user;

public class DuplicateEmailException extends Exception {

    public DuplicateEmailException() {
        super("A user with this email already exists.");
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}