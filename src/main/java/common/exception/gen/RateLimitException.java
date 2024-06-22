package common.exception.gen;


/**
 * Exception for when the rate limit is exceeded. Can be used by all functions that have rate limits.
 */
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
