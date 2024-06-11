package service.app.footballService.fixtureOdds.exception;

public class InvalidResponseException extends Exception {
    public InvalidResponseException(String message) {
        super(message);
    }

    public InvalidResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
