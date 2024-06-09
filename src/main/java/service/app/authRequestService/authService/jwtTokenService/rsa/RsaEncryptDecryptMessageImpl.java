package service.app.authRequestService.authService.jwtTokenService.rsa;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * Implementation of the RsaKeyGenerator interface.<br>
 * WARNING: The key is only stored in memory and will be lost when the application is closed,
 * meaning that dependant services will be invalidated.
 */
@Component
public class RsaEncryptDecryptMessageImpl implements RsaEncryptDecryptMessage {

    /**
     * Encrypts a message using the provided public key.
     *
     * @param message   the message to encrypt
     * @param rsaKey the public key to use for encryption {@see RsaKeyGenerator}
     * @return the encrypted message
     */
    @Override
    public String encryptMessage(String message, RSAKey rsaKey)
    throws JOSEException {
        JWEObject jweObject = new JWEObject(
                new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM),
                new Payload(message)
        );
        jweObject.encrypt(new RSAEncrypter(rsaKey));
        return jweObject.serialize();
    }

    /**
     * Decrypts a message using the provided public key.
     *
     * @param message    the message to encrypt
     * @param rsaKey the public key to use for encryption {@see RsaKeyGenerator}
     * @return the encrypted message
     */
    @Override
    public String decryptMessage(String message, RSAKey rsaKey)
    throws JOSEException, ParseException {
        JWEObject jweObject = JWEObject.parse(message);
        jweObject.decrypt(new RSADecrypter(rsaKey.toRSAPrivateKey()));
        return jweObject.getPayload().toString();
    }
}
