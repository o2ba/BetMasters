package service.app.authRequestService.authService.refreshTokenService.token;

import common.security.NonSensitiveData;
import org.springframework.stereotype.Component;

@Component
public interface RefreshTokenGenerator {
    NonSensitiveData generateRefreshToken();
}

