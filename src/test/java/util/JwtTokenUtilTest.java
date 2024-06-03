package util;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import common.util.auth.JwtTokenUtil;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    void testGenerateToken() {
        try {
            String token = jwtTokenUtil.generateEncryptedToken("test", 1);
            assertNotNull(token);
        } catch (JOSEException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testEncryptToken() {
        try {
            String token = jwtTokenUtil.generateEncryptedToken("test", 1);
            String encryptedToken = jwtTokenUtil.encryptToken(token);
            assertNotNull(encryptedToken);
            assertNotEquals(token, encryptedToken);
        } catch (JOSEException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDecryptToken() {
        try {
            String token = jwtTokenUtil.generateEncryptedToken("test", 1);
            String encryptedToken = jwtTokenUtil.encryptToken(token);
            String decryptedToken = jwtTokenUtil.decryptToken(encryptedToken);
            assertNotNull(decryptedToken);
            assertEquals(token, decryptedToken);
        } catch (JOSEException e) {
            fail(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Test to verify a token.
     * This is equivalent to a user providing us a token and us verifying it.
     */
    @Test
    void testVerifyToken() {
        try {
            String token = jwtTokenUtil.generateEncryptedToken("myemailaddress", 1);
            String decryptedToken = jwtTokenUtil.decryptToken(token);
            assertNotNull(jwtTokenUtil.verifyToken(decryptedToken));
            String userEmail = jwtTokenUtil.verifyToken(decryptedToken).getSubject();
            assertEquals("myemailaddress", userEmail);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    /**
     * Test that an invalid token throws an exception.
     */
    @Test
    void testInvalidToken() {
        try {
            jwtTokenUtil.verifyToken("inv.al.idtoken");
            fail("Invalid token did not throw an exception");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(e instanceof Exception);
        }
    }

    /** Create an expired token and test that it throws an exception. */
    @Test
    void testExpiredToken() {
        try {
            String token = jwtTokenUtil.generateEncryptedToken("myemailaddress", 1, -1);
            String decryptedToken = jwtTokenUtil.decryptToken(token);
            // get subject
            String userEmail = jwtTokenUtil.verifyToken(decryptedToken).getSubject();
            System.out.println(userEmail);
            assertEquals("myemailaddress", userEmail);
            jwtTokenUtil.verifyToken(decryptedToken);
            fail("Expired token did not throw an exception");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(e instanceof Exception);
        }
    }
}