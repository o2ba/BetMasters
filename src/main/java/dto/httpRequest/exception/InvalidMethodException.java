package dto.httpRequest.exception;

public class InvalidMethodException extends IllegalArgumentException {
    public InvalidMethodException(String message) {
        super(message);
    }
}
