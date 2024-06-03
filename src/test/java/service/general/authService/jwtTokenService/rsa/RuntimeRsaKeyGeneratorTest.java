package service.general.authService.jwtTokenService.rsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeRsaKeyGeneratorTest {
    @Test
    void getKey_sameInstance() {
        RuntimeRsaKeyGenerator generator = new RuntimeRsaKeyGenerator();
        assertSame(generator.getKey(), generator.getKey(), "getKey should always return the same instance");
    }

    @Test
    void getKey_notNull() {
        RuntimeRsaKeyGenerator generator = new RuntimeRsaKeyGenerator();
        assertNotNull(generator.getKey(), "getKey should never return null");
    }
}