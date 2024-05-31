package exception.user;

public class RateLimitException extends Exception {
    public RateLimitException() {
        super("Too many requests");
    }

    public RateLimitException(String message) {
        super(message);
    }
    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
