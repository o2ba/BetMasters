package exception.login;

public class InvalidPasswordError extends Exception {
    public InvalidPasswordError() {
        super("Invalid password");
    }
}
