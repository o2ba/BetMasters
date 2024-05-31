package util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;


/**
 * Utility class for generating and verifying JWT tokens.
 */
public class JwtUtil {

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
     *     <li>sub: the user id</li>
     *     <li>exp: the expiration time of the JWT</li>
     *     <li>iat: the time the JWT was issued</li>
     *     <li>username: the username of the user</li>
     * </ul>
     * @param username the username of the user (typically the email)
     * @param uid the user id
     * @param expiryIn the expiration time of the JWT in seconds
     * @return the JWT payload
     */
    public String buildToken(String username, int uid, long expiryIn) {

        long timeNowInUnix = System.currentTimeMillis() / 1000L;

        Map<String, String> claims = Map.of(
                "iss", "https://betmasters.cc",
                "sub", String.valueOf(uid),
                "exp", String.valueOf(timeNowInUnix + expiryIn),
                "iat", String.valueOf(timeNowInUnix),
                "username", username
        );

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
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
    throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
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
