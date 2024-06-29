package service.general.authService.jwtTokenService.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenImplTest {
    private JwtTokenImpl jwtToken;
    private String subject;
    private int uid;
    private long lifetime;
    private String secretKey;

    @BeforeEach
    void setUp() {
        jwtToken = new JwtTokenImpl();
        subject = "bade@obari.de";
        uid = 123;
        lifetime = 1000L; // 1 second
        secretKey = UUID.randomUUID().toString(); // generate a secret key
    }

    @Test
    void generateTokenWithSecret() {
        String token = jwtToken.generateToken(subject, uid, lifetime, secretKey);
        assertNotNull(token, "Generated token should not be null");
        System.out.println(token);
        assertTrue(token.split("\\.").length == 3, "Generated token should have three parts separated by '.'");
    }

    @Test
    void decodeTokenWithSecret() {
        String token = jwtToken.generateToken(subject, uid, lifetime, secretKey);
        DecodedJWT decodedToken = jwtToken.decodeToken(token, secretKey);

        assertEquals(subject, decodedToken.getSubject(), "Decoded token should have the same subject as the original");
        assertEquals(uid, decodedToken.getClaim("uid").asInt(), "Decoded token should have the same uid as the original");
    }

    // Decode with wrong secret key
    @Test
    void decodeTokenWithWrongSecret() {
        String token = jwtToken.generateToken(subject, uid, lifetime, secretKey);
        String wrongSecretKey = UUID.randomUUID().toString(); // generate a wrong secret key

        assertThrows(com.auth0.jwt.exceptions.JWTVerificationException.class, () -> jwtToken.decodeToken(token, wrongSecretKey), "Decoding a token with a wrong secret key should throw a JWTVerificationException");
    }

    @Test
    void decodeTokenWithSecret_withExpiredToken() {
        String token = jwtToken.generateToken(subject, uid, 1L, secretKey); // 1 millisecond
        try {
            Thread.sleep(2L); // wait for the token to expire
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThrows(com.auth0.jwt.exceptions.TokenExpiredException.class, () -> jwtToken.decodeToken(token, secretKey), "Decoding an expired token should throw a TokenExpiredException");
    }
}