package service.general.external.emailService.exception;

/**
 * Exception thrown when the email could not be sent
 */
public class EmailSendingException extends Exception {
    public EmailSendingException(String message) {
        super(message);
    }
}
