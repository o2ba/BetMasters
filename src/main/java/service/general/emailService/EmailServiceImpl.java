package service.general.emailService;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;

public class EmailServiceImpl implements EmailService {

    @Value("${resend.host.mail}")
    private static String hostMail;

    @Value("${resend.api.key}")
    private static String apiKey;

    @Override
    public void sendEmail(String recipient, String subject, String message) throws ResendException {
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
            throw new ResendException("Error sending email");
        }
    }

    public static void main(String[] args) {
        EmailServiceImpl emailService = new EmailServiceImpl();
        try {
            emailService.sendEmail("bade@obari.de"
                    , "Test"
                    , "This is a test email");
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendVerificationEmail(String recipient, String token) throws ResendException {

    }

    @Override
    public void sendPasswordResetEmail(String recipient, String token) throws ResendException {

    }
}
