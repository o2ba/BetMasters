package service.general.authService.jwtTokenService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import run.Main;
import service.general.authService.jwtTokenService.jwt.JwtToken;
import service.general.authService.jwtTokenService.rsa.RsaEncryptDecryptMessage;
import service.general.authService.jwtTokenService.rsa.RuntimeRsaKeyGenerator;
import service.general.authService.jwtTokenService.jwt.RuntimeSecretKeyGenerator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
class EncryptedJwtTokenServiceTest {

    @Autowired
    private EncryptedJwtTokenService encryptedJwtTokenService;

    @Autowired
    private RsaEncryptDecryptMessage rsaEncryptDecryptMessage;

    @Autowired
    private JwtToken jwtToken;

    @SpyBean
    private RuntimeRsaKeyGenerator runtimeRsaKeyGenerator;

    @SpyBean
    private RuntimeSecretKeyGenerator runtimeSecretKeyGenerator;

    @Test
    void generateEncryptedToken() throws Exception {
        encryptedJwtTokenService.generateEncryptedToken(
                "person@example.org", 1, 1000
        );
    }


    // No error cases
    @Test
    void verifyEncryptedToken() throws Exception {
        String token = encryptedJwtTokenService.generateEncryptedToken(
                "person@example.org", 1, 3000
        );

        // All goes good. The token is verified.
        assertTrue(encryptedJwtTokenService.verifyEncryptedToken(token,
                "person@example.org",
                1));
        // The subject is wrong. The token is not verified.
        assertFalse(encryptedJwtTokenService.verifyEncryptedToken(token,
                "person2@example.org",
                1));
        // The uid is wrong. The token is not verified.
        assertFalse(encryptedJwtTokenService.verifyEncryptedToken(token,
                "person@example.org",
                10));


        // Sleep to make the token expire
        Thread.sleep(3000);

        // The token is expired. The token is not verified.
        assertFalse(encryptedJwtTokenService.verifyEncryptedToken(token,
                "person@example.org",
                1));

    }


    // Verifying with a wrong secret key
    @Test
    void verifyEncryptedTokenWrongSecretKey() throws Exception {
        String token = encryptedJwtTokenService.generateEncryptedToken(
                "person@example.org", 1, 3000
        );

        // Verify the token with the correct secret key
        assertTrue(encryptedJwtTokenService.verifyEncryptedToken(token, "person@example.org", 1));

        Mockito.doReturn("wrongSecretKey").when(runtimeSecretKeyGenerator).getKey();

        // Verify the token with the wrong secret key
        assertFalse(encryptedJwtTokenService.verifyEncryptedToken(token, "person@example.org", 1));
    }


    // verify with wrong RSA key
    @Test
    void verifyEncryptedTokenWrongRsaKey() throws Exception {
        String token = encryptedJwtTokenService.generateEncryptedToken(
                "person@example.org", 1, 3000
        );

        // Verify the token with the correct RSA key
        assertTrue(encryptedJwtTokenService.verifyEncryptedToken(token, "person@example.org", 1));

        Mockito.doReturn(null).when(runtimeRsaKeyGenerator).getKey();

        // Verify the token with the wrong RSA key
        assertFalse(encryptedJwtTokenService.verifyEncryptedToken(token, "person@example.org", 1));
    }


}