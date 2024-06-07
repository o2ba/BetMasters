package service.app.__deprecated.userService.publicRequest.verifyEmail;

public interface EmailVerificationIncoming {
    void sendVerificationEmail(String email, String token);
    void verifyEmail(String token);
}
