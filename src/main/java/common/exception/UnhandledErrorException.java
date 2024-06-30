package common.exception;

/**
 * Exception thrown when an unhandled exception occurs
 */
public class UnhandledErrorException extends Exception {
    public UnhandledErrorException(String message) {
        super(message);
    }

    public UnhandledErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnhandledErrorException(Throwable cause) {
        super(cause);
    }
}
