package dto.request.httpRequest.exception;

public class ResponseParseException extends Exception {
    public ResponseParseException(String message) {
        super(message);
    }

    public ResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
