package service.general.authService.jwtTokenService.jwt;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RuntimeSecretKeyGenerator {
    private static final String SECRET_KEY = generateSecretKey();

    private static String generateSecretKey() {
        return UUID.randomUUID().toString();
    }

    public String getKey() {
        return SECRET_KEY;
    }
}
