package service.general.emailService;

import com.resend.core.exception.ResendException;

public interface EmailService {
    /**
     * Generic method to send an email.
     * @param recipient The email address of the recipient
     * @param subject The subject of the email
     * @param message The message of the email
     */
    void sendEmail(String recipient, String subject, String message) throws ResendException;

    /**
     * Method to send a verification email.
     * @param recipient The email address of the recipient
     * @param token The token to be sent
     */
    void sendVerificationEmail(String recipient, String token) throws ResendException;

    /**
     * Method to send a password reset email.
     * @param recipient The email address of the recipient
     * @param token The token to be sent
     */
    void sendPasswordResetEmail(String recipient, String token) throws ResendException;
}
