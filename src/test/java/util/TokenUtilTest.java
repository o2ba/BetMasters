package util;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.auth.TokenUtil;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    private TokenUtil tokenUtil;

    @BeforeEach
    void setUp() {
        tokenUtil = new TokenUtil();
    }

    @Test
    void testGenerateToken() {
        try {
            String token = tokenUtil.generateEncryptedToken("test", 1);
            assertNotNull(token);
        } catch (JOSEException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testEncryptToken() {
        try {
            String token = tokenUtil.generateEncryptedToken("test", 1);
            String encryptedToken = tokenUtil.encryptToken(token);
            assertNotNull(encryptedToken);
            assertNotEquals(token, encryptedToken);
        } catch (JOSEException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDecryptToken() {
        try {
            String token = tokenUtil.generateEncryptedToken("test", 1);
            String encryptedToken = tokenUtil.encryptToken(token);
            String decryptedToken = tokenUtil.decryptToken(encryptedToken);
            assertNotNull(decryptedToken);
            assertEquals(token, decryptedToken);
        } catch (JOSEException e) {
            fail(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testVerifyToken() {
        try {
            String token = tokenUtil.generateEncryptedToken("test", 1);
            String decryptedToken = tokenUtil.decryptToken(token);
            assertNotNull(tokenUtil.verifyToken(decryptedToken));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}