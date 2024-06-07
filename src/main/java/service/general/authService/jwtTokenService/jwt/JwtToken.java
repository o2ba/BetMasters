package service.general.authService.jwtTokenService.jwt;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

/**
 * Interface for creating a JWT token.
 */
@Component
public interface JwtToken {

    /**
     * Generate a JWT Token.
     *
     * @param subject the subject of the token
     * @param uid the user id of the token
     * @param lifetime the expiration time of the token
     * @return the generated token
     * @deprecated use signed tokens instead
     */
    @Deprecated
    String generateToken(String subject, int uid, long lifetime);

    /**
     * Generate a JWT Token. Signed with a secret key
     *
     * @param subject the subject of the token
     * @param uid the user id of the token
     * @param lifetime the expiration time of the token
     * @return the generated token
     */
    String generateToken(String subject, int uid, long lifetime, String secretKey);


    /**
     * Decodes a JWT token
     *
     * @param token the token to decode
     * @return the decoded token
     */
    @Deprecated
    DecodedJWT decodeToken(String token) throws JWTVerificationException;

    /**
     * Decodes a JWT token
     *
     * @param token the token to decode
     * @param secretKey the secret key to decode the token
     * @return the decoded token
     */
    DecodedJWT decodeToken(String token, String secretKey) throws JWTVerificationException;
}
