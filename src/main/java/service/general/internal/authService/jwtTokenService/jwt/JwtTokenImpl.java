package service.general.internal.authService.jwtTokenService.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenImpl implements JwtToken {

    /**
     * Generate a JWT Token. Not signed
     *
     * @param subject the subject of the token
     * @param uid the user id of the token
     * @param lifetime the expiration time of the token
     * @return the generated token
     * @deprecated use signed tokens instead
     */
    @Deprecated
    public String generateToken(String subject, int uid, long lifetime) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("uid", uid)
                .withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
                .sign(Algorithm.none());
    }

    @Override
    public String generateToken(String subject, int uid, long lifetime, String secretKey) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("uid", uid)
                .withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
                .sign(Algorithm.HMAC256(secretKey));
    }


    /**
     * Decodes a JWT token
     *
     * @param token the token to decode
     * @return the decoded token
     */
    @Override
    @Deprecated
    public DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.none()).build();
        return verifier.verify(token);
    }

    /**
     * Decodes a JWT token
     *
     * @param token the token to decode
     * @return the decoded token
     */
    @Override
    public DecodedJWT decodeToken(String token, String secretKey) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        return verifier.verify(token);
    }


}
