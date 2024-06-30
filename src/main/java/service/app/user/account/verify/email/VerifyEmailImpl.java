package service.app.user.account.verify.email;

import com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.RandomStringUtils;
import common.exception.UnhandledErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.user.account.verify.email.exception.EmailNotFoundException;
import service.general.external.emailService.EmailService;

@Service
public class VerifyEmailImpl implements VerifyEmail {

    EmailService emailService;

    @Autowired
    public VerifyEmailImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendVerificationEmail(String recipient, int lifetime) throws EmailNotFoundException, UnhandledErrorException {

    }

    private String generateToken(String email, int lifetime) {
        String token = RandomStringUtils.randomAlphanumeric(60);
        System.out.println("Token: " + token);  // Debug statement
        return token;
    }

}