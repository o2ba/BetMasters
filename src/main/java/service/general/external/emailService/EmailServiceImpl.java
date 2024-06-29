package service.general.external.emailService;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.general.external.emailService.exception.EmailSendingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${resend.host.mail}")
    private String hostMail;

    @Value("${resend.api.key}")
    private String apiKey;

    @Override
    public void sendEmail(String recipient, String subject, String templateName, Map<String, String> values) throws EmailSendingException {
        try {
            String template = loadTemplate(templateName);
            String emailHtml = populateTemplate(template, values);
            sendEmail(recipient, subject, emailHtml);
        } catch (IOException e) {
            logger.error("Error loading email template (IOException)", e);
            throw new EmailSendingException("Error loading email template");
        } catch (ResendException e) {
            logger.error("Error sending email (ResendException)", e);
            throw new EmailSendingException("Error sending email");
        }
    }

    private void sendEmail(String recipient, String subject, String message) throws ResendException {
        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(hostMail)
                .to(recipient)
                .subject(subject)
                .html(message)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
        } catch (Exception e) {
            throw new ResendException("Error sending email", e);
        }
    }

    private String loadTemplate(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("File not found: " + fileName);
        }
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    private String populateTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
