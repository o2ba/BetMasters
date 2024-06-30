package common.exception.gen;

public class UserUnderAgeException extends Exception {
    public UserUnderAgeException() {
        super("User is under age");
    }

    public UserUnderAgeException(String message) {
        super(message);
    }

    public UserUnderAgeException(String message, Throwable cause) {
        super(message, cause);
    }
}
