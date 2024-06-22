package service.app.authRequestService.authService.refreshTokenService.token;

import common.model.security.NonSensitiveData;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.security.SecureRandom;

@Service
public class RefreshTokenGeneratorImpl implements RefreshTokenGenerator {

    private final SecureRandom secureRandom;

    public RefreshTokenGeneratorImpl() {
        secureRandom = new SecureRandom();
    }

    public NonSensitiveData generateRefreshToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return new NonSensitiveData(Base64.getUrlEncoder().encodeToString(randomBytes));
    }
}