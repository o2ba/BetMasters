package main.java.exception.sql;

public class WrongPasswordRateLimitException extends Exception {
    public WrongPasswordRateLimitException() {
        super("Too many failed attempts. Please wait before trying again.");
    }

    public WrongPasswordRateLimitException(String message) {
        super(message);
    }
    public WrongPasswordRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
