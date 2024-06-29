package service.app.user.activity.transact.exception;

/**
 * Exception thrown when the transaction is invalid
 */
public class InvalidTransactionException extends Exception {
    public enum Type {
        INVALID_AMOUNT,
        INVALID_TRANSACTION_TYPE
    }

    public InvalidTransactionException(Type type) {
        super(type.toString());
    }

    public InvalidTransactionException(String message) {
        super(message);
    }

    public Type getType() {
        return Type.valueOf(getMessage());
    }
}
