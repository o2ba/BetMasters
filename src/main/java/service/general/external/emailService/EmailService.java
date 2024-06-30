package service.general.external.emailService;


import service.general.external.emailService.exception.EmailSendingException;

import java.util.Map;

/**
 * EmailService provides functionality for sending emails. This service is responsible for
 * loading email templates, populating them with data, and sending the emails.
 */
public interface EmailService {

    /**
     * Sends an email to the specified recipient with the given subject and content populated
     * from a template.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Loads the specified email template.</li>
     *     <li>Populates the template with the provided values.</li>
     *     <li>Sends the populated email to the recipient with the specified subject.</li>
     * </ol>
     *
     * <p>The method throws an {@link EmailSendingException} if there is an error in any of
     * these steps.</p>
     *
     * @param recipient The email address of the recipient. Must not be null or empty.
     * @param subject The subject of the email. Must not be null or empty.
     * @param templateName The name of the template file to load. Must not be null or empty.
     * @param values A map of placeholder names and their corresponding values to replace in
     *               the template. Must not be null, but can be empty if the template does not
     *               require any placeholders to be replaced.
     * @throws EmailSendingException If there is an error in loading the template, populating
     *                               the template, or sending the email.
     */
    void sendEmail(String recipient, String subject, String templateName, Map<String, String> values)
            throws EmailSendingException;
}
