package exception.user;

public class UserAuthException extends Exception {

    public static final String MESSAGE_ID = "user.auth.invalid";

    public UserAuthException(String message) {
        super(message);
    }
}
