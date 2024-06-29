package service.general.authService.jwtTokenService.rsa;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class RsaEncryptDecryptMessageImplTest {

    private RsaEncryptDecryptMessageImpl rsaEncryptDecryptMessage;
    private RSAKey rsaKey;

    @BeforeEach
    void setUp() {
        rsaEncryptDecryptMessage = new RsaEncryptDecryptMessageImpl();
        rsaKey = new RuntimeRsaKeyGenerator().getKey();
    }

    @Test
    void encryptMessage() throws JOSEException {
        String message = "Hello, World!";
        String encryptedMessage = rsaEncryptDecryptMessage.encryptMessage(message, rsaKey);

        assertNotEquals(message, encryptedMessage,
                "The encrypted message should not be the same as the original message");
    }

    @Test
    void decryptMessage() throws JOSEException, ParseException {
        String message = "Hello, World!";
        String encryptedMessage = rsaEncryptDecryptMessage.encryptMessage(message, rsaKey);
        String decryptedMessage = rsaEncryptDecryptMessage.decryptMessage(encryptedMessage, rsaKey);

        assertEquals(message, decryptedMessage,
                "The decrypted message should be the same as the original message");
    }

    @Test
    void decryptMessage_withWrongKey() throws JOSEException {
        String message = "Hello, World!";
        String encryptedMessage = rsaEncryptDecryptMessage.encryptMessage(message, rsaKey);

        // Mock the RuntimeRsaKeyGenerator to return a different key
        RuntimeRsaKeyGenerator mockGenerator = Mockito.mock(RuntimeRsaKeyGenerator.class);
        RSAKey wrongKey = new RSAKeyGenerator(2048).generate();
        Mockito.when(mockGenerator.getKey()).thenReturn(wrongKey);

        assertThrows(JOSEException.class, () -> rsaEncryptDecryptMessage.decryptMessage(encryptedMessage, wrongKey),
                "Decrypting with the wrong key should throw a JOSEException");
    }
}