package util.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.RSAKey;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

import static java.security.KeyRep.Type.SECRET;

/**
 * Utility class for generating and verifying JWT tokens.
 */
public class TokenUtil {

    /**
     * Static RSA key pair used for encrypting and decrypting JWT tokens.
     * In our case, this is generated at runtime, and not stored in a file.
     * Although this means that the key pair is different every time the server is started,
     * it is not a problem as users are able to refresh their tokens using the refresh token.
     * @see RefreshTokenUtil
     */
    private static final RSAKey RSA_KEY = generateRSAKeyPair();

    /**
     * Default lifetime of a JWT token in milliseconds.
     */
    private static final long DEFAULT_LIFETIME =  1000 * 60 * 60; // 1 hour

    // -- Private methods -- //

    /**
     * Generates a new RSA key pair.
     * @return the RSA key pair
     */
    private static RSAKey generateRSAKeyPair() throws RuntimeException {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate()).build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    /**
     * Non-overloaded method to generate a JWT token. Token is not encrypted.
     * @param subject the subject of the token
     * @return the JWT token
     */
    private String generateToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + DEFAULT_LIFETIME))
                .sign(Algorithm.none());
    }

    /**
     * Encrypts a JWT token using the RSA algorithm.
     * @param payload the token to encrypt
     *
     */
    public String encryptToken(String payload) throws JOSEException {

        JWEObject jweObject = new JWEObject(
            new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM),
            new Payload(payload)
        );
        jweObject.encrypt(new RSAEncrypter(RSA_KEY));
        return jweObject.serialize();
    }


    // -- Public methods -- //

    /**
     * Generates an encrypted JWT token.
     */
    public String generateEncryptedToken(String subject) throws JOSEException {
        String payload = generateToken(subject);
        return encryptToken(payload);
    }

    /**
     * Decrypts a JWT token using the RSA algorithm.
     * @param jweString the encrypted token
     * @return the decrypted token
     */
    public String decryptToken(String jweString) throws ParseException, JOSEException{
        JWEObject jweObject = JWEObject.parse(jweString);
        jweObject.decrypt(new RSADecrypter(RSA_KEY.toRSAPrivateKey()));
        return jweObject.getPayload().toString();
    }

    /**
     * Verifies a JWT token.
     * @param jweString the encrypted token
     * @return the decoded token
     */
    public DecodedJWT verifyToken(String jweString) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.none()).build();
        System.out.println(jweString);
        return verifier.verify(jweString);
    }


}

