package service.app.user.activity.betv2.exception;

public class InvalidInputException extends Exception {
    public enum Type {
        INVALID_FIXTURE, // The fixture does not exist
        INVALID_BET, // The bet is invalid
        INVALID_PREDICTION // The prediction is invalid
    }

    public InvalidInputException(Type type) {
        super(type.toString());
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public Type getType() {
        return Type.valueOf(getMessage());
    }
}
