package util.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating and verifying JWT tokens.
 */
public class TokenUtil {

    /**
     * The secret key used to sign the JWT.
     */
    private static final String jwtSecret;

    /*
     * Initializes the jwtSecret field. If the JWT_SECRET environment variable is not set, a RuntimeException is thrown.
     */
    static {
        jwtSecret = System.getenv("JWT_SECRET");
        if (jwtSecret == null) {
            throw new RuntimeException("JWT_SECRET environment variable is not set");
        }
    }

    /**
     * Generates a JWT token with
     * <ul>
     *     <li>iss: "<a href="https://betmasters.cc">https://betmasters.cc"</a>"</li>
     *     <li>sub: the user email</li>
     *     <li>exp: the expiration time of the JWT</li>
     *     <li>iat: the time the JWT was issued</li>
     * </ul>
     * @param email the username of the user (typically the email)
     * @param expiryIn the expiration time of the JWT in seconds
     * @return the JWT payload
     */
    public String buildToken(String email, long expiryIn) throws JOSEException {

        long timeNowInUnix = System.currentTimeMillis() / 1000L;

        Map<String, String> claims = Map.of(
                "iss", "https://betmasters.cc",
                "sub", email,
                "exp", String.valueOf(timeNowInUnix + expiryIn),
                "iat", String.valueOf(timeNowInUnix)
        );

        Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        JWSSigner signer = new MACSigner(signingKey.getEncoded());

        // Create the JWT Claims Set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(claims.get("iss"))
                .subject(claims.get("sub"))
                .expirationTime(new Date(Long.parseLong(claims.get("exp")) * 1000))
                .issueTime(new Date(Long.parseLong(claims.get("iat")) * 1000))
                .build();

        // Create the signed JWT
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet);

        signedJWT.sign(signer);

        // Create a random AES key for encryption
        SecureRandom random = new SecureRandom();
        byte[] sharedKey = new byte[32];
        random.nextBytes(sharedKey);
        SecretKey encryptionKey = Keys.hmacShaKeyFor(sharedKey);

        // Create the JWE object with the signed JWT as payload
        JWEObject jweObject = new JWEObject(
                new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM),
                new Payload(signedJWT));

        // Encrypt the JWE object
        jweObject.encrypt(new DirectEncrypter(encryptionKey.getEncoded()));

        // Return the encrypted JWT
        return jweObject.serialize();
    }

    /**
     * Decodes the JWT token and returns the claims.
     * @param token the JWT token
     * @return the claims of the JWT token
     * @throws MalformedJwtException if the token is malformed
     * @throws ExpiredJwtException if the token is expired
     * @throws UnsupportedJwtException if the token is unsupported
     * @throws IllegalArgumentException if the token is invalid
     */
    public Claims decodeToken(String token)
            throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException, JOSEException, java.text.ParseException {
        // Parse the JWE token
        JWEObject jweObject = JWEObject.parse(token);

        // Decrypt the JWE token
        SecretKey encryptionKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        jweObject.decrypt(new DirectDecrypter(encryptionKey.getEncoded()));

        // Get the signed JWT
        SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

        // Verify the signed JWT
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        JWSVerifier verifier = new MACVerifier(signingKey.getEncoded());

        if (!signedJWT.verify(verifier)) {
            throw new IllegalArgumentException("JWT signature does not match locally computed signature");
        }

        // Get the token's claims
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        Date expirationTime = claimsSet.getExpirationTime();
        if (expirationTime.before(new Date())) {
            throw new ExpiredJwtException(null, null, "Token has expired");
        }

        // Convert the claims to JJWT Claims object
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("iss", claimsSet.getIssuer());
        claimsMap.put("sub", claimsSet.getSubject());
        claimsMap.put("exp", claimsSet.getExpirationTime());
        claimsMap.put("iat", claimsSet.getIssueTime());

        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        claims.putAll(claimsMap);

        return claims;
    }

    /**
     * Verifies the JWT token.
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean verifyToken(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

