package service.general.authService.jwtTokenService;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.JOSEException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.general.authService.jwtTokenService.jwt.JwtToken;
import service.general.authService.jwtTokenService.jwt.RuntimeSecretKeyGenerator;
import service.general.authService.jwtTokenService.rsa.RsaEncryptDecryptMessage;
import service.general.authService.jwtTokenService.rsa.RuntimeRsaKeyGenerator;

import java.text.ParseException;

@Service
public final class JwtTokenServiceImpl implements JwtTokenService {

    RsaEncryptDecryptMessage rsaEncryptDecrypt;
    JwtToken jwtToken;
    RuntimeRsaKeyGenerator runtimeRsaKeyGenerator;
    RuntimeSecretKeyGenerator runtimeSecretKeyGenerator;

    @Autowired
    public JwtTokenServiceImpl(RsaEncryptDecryptMessage rsaEncryptDecrypt,
                               JwtToken jwtToken,
                               RuntimeRsaKeyGenerator runtimeRsaKeyGenerator,
                               RuntimeSecretKeyGenerator runtimeSecretKeyGenerator) {
        this.rsaEncryptDecrypt = rsaEncryptDecrypt;
        this.jwtToken = jwtToken;
        this.runtimeRsaKeyGenerator = runtimeRsaKeyGenerator;
        this.runtimeSecretKeyGenerator = runtimeSecretKeyGenerator;
    }

    /**
     * Generates an RSA-Encrypted JWT token for the given subject and user ID.
     *
     * @param subject  the subject of the token
     * @param uid      the user ID
     * @param lifetime the lifetime of the token in milliseconds
     * @return the generated token
     */
    @NotNull
    @Override
    public String generateEncryptedToken(String subject, int uid, Long lifetime)
    throws JOSEException {
        String token = jwtToken.generateToken(subject, uid, lifetime, runtimeSecretKeyGenerator.getKey());
        return rsaEncryptDecrypt.encryptMessage(token, runtimeRsaKeyGenerator.getKey());
    }

    /**
     * Checks if a token corresponds to a user.
     *
     * @param token   the token to verify
     * @param subject the subject to verify (usually email)
     * @param uid     the user ID
     * @return true if the token is valid, false otherwise
     */
    public boolean verifyEncryptedToken(String token, String subject, int uid) {
        try {
            DecodedJWT decodedToken = decodeToken(token, runtimeSecretKeyGenerator.getKey());
            return decodedToken.getClaim("uid").asInt() == uid && decodedToken.getSubject().equals(subject);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Decodes a JWT token. Invalid tokens cannot be decoded and will throw a JWTVerificationException.
     * Not recommend for use. To validate a token, use {@link #verifyEncryptedToken(String, String, int)}.
     *
     * @param token the token to read
     * @return the user ID
     * @throws JWTVerificationException if the token is invalid
     * @throws ParseException if the token cannot be parsed
     * @throws JOSEException if the token cannot be decrypted
     * @see DecodedJWT
     */
    public DecodedJWT decodeToken(String token, String secretKey) throws JWTVerificationException, ParseException, JOSEException {
        String decryptedToken = rsaEncryptDecrypt.decryptMessage(token, runtimeRsaKeyGenerator.getKey());
        return jwtToken.decodeToken(decryptedToken, secretKey);
    }

}