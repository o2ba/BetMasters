package service.emailVerification;

public interface IEmailVerificationIncoming {
    void sendVerificationEmail(String email, String token);
    void verifyEmail(String token);
}
