package exception.login;

public class WrongEmailPasswordException extends Exception {

    public static final String MESSAGE_ID = "login.failure";

    public WrongEmailPasswordException() {
        super("The email and password combination is incorrect.");
    }
}
