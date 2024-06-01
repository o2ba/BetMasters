package util;

import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.auth.TokenUtil;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    private TokenUtil tokenUtil;

    @BeforeEach
    void setUp() {
        tokenUtil = new TokenUtil();
    }

    @Test
    void testBuildToken() throws JOSEException {
        String token = tokenUtil.buildToken("test@betmasterc.cc", 15);
        assertNotNull(token);
    }

    @Test
    void testDecodeToken() throws JOSEException, ParseException {
        String token = tokenUtil.buildToken("test@betmasterc.cc", 15);
        Claims claims = tokenUtil.decodeToken(token);

        assertFalse(claims.isEmpty());
        assertEquals("test@betmasterc.cc", claims.get("sub"));
        assertEquals("https://betmasters.cc", claims.get("iss"));
    }

    @Test
    void testDecodeTokenWithInvalidToken() {
        String invalidToken = "invalid token";
        assertThrows(MalformedJwtException.class, () -> tokenUtil.decodeToken(invalidToken));
    }

    @Test
    void testDecodeTokenWithExpiredToken() throws JOSEException {
        String expiredToken = tokenUtil.buildToken("test@betmasterc.cc", -15);
        assertThrows(ExpiredJwtException.class, () -> tokenUtil.decodeToken(expiredToken));
    }

    @Test
    void testVerifyToken() throws JOSEException {
        String token = tokenUtil.buildToken("test@betmasterc.cc", 15);
        assertTrue(tokenUtil.verifyToken(token));
    }

    @Test
    void testVerifyTokenWithInvalidToken() {
        String invalidToken = "invalid token";
        assertFalse(tokenUtil.verifyToken(invalidToken));
    }
}