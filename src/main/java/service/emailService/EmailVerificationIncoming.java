package service.emailService;

public interface EmailVerificationIncoming {
    void sendVerificationEmail(String email, String token);
    void verifyEmail(String token);
}
