package service.app.authRequestService.simpleBettingService.exceptions;

public class GameAlreadyStartedOrCancelledException extends Exception {
    public GameAlreadyStartedOrCancelledException(String message) {
        super(message);
    }
}
