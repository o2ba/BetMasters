package service.app.fixture.common.exception;

public class RequestSendingException extends Exception {
    public RequestSendingException(String message) {
        super(message);
    }

    public RequestSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
