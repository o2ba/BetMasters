package service.app.user.account.verify.email.exception;

public class UserAlreadyVerified extends Exception {
    public UserAlreadyVerified(String message) {
        super(message);
    }
}
