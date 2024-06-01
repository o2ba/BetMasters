package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import util.auth.TokenUtil;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    TokenUtil jwtUtil = new TokenUtil();

    @Test
    void buildHeader() {
    }

    @Test
    void buildPayload() {
        assertDoesNotThrow(() -> jwtUtil.buildToken("test", 1, 15));
    }

    /*
     * Tests a valid token, an invalid token and an expired token.
     */
    @Test
    void decodeToken() {
        String token = jwtUtil
                .buildToken("test@betmasterc.cc", 1, 15);
        Claims claims = jwtUtil.decodeToken(token);

        assertFalse(claims.isEmpty());
        assertEquals("test@betmasterc.cc", claims.get("username"));
        assertEquals("1", claims.get("sub"));
        assertEquals("https://betmasters.cc", claims.get("iss"));

        String invalidToken = "invalid token";
        assertThrows(MalformedJwtException.class, () -> jwtUtil.decodeToken(invalidToken));

        String expiredToken = jwtUtil
                .buildToken("test@betmasterc.cc", 1, -15);
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.decodeToken(expiredToken));

    }

    @Test
    void verifyToken() {
        String token = jwtUtil
                .buildToken("test", 1, 15);
        assertTrue(jwtUtil.verifyToken(token));
        assertFalse(jwtUtil.verifyToken("invalid token"));

    }
}