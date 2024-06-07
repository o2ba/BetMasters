package service.general.authService.jwtTokenService.rsa;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;


/**
 * Static class for generating an RSA key at runtime.<br>
 * Methods:<br>
 * - getKey(): RSAKey - Gets the generated RSA key.
 */
@Component
public class RuntimeRsaKeyGenerator {

    /**
     * The generated RSA key.
     */
    private static final RSAKey RSA_KEY;

    static {
        try {
            RSA_KEY = generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a key.
     *
     * @return the generated key
     */
    private static RSAKey generateKey()
            throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate()).build();
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public RSAKey getKey() {
        return RSA_KEY;
    }
}
