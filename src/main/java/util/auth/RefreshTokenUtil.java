package util.auth;

import object.security.EncryptedData;
import object.security.NonSensitiveData;

import java.time.LocalDateTime;
import java.util.Base64;
import java.security.SecureRandom;


/**
 * Utility class for generating and validating refresh tokens.
 * Refresh tokens are used for persistent login.
 */
public class RefreshTokenUtil {

    /** Parameters for the refresh token. */
    private static final int DURATION_IN_DAYS = 15; // 15 days

    /** Secure random number generator for generating refresh tokens. */
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a refresh token.
     * @return a refresh token
     */
    public NonSensitiveData generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return new NonSensitiveData(Base64.getUrlEncoder().encodeToString(randomBytes));
    }

    /**
     * Hashes the refresh token.
     * @param token the refresh token
     * @return the hashed refresh token
     */
    public EncryptedData hashToken(NonSensitiveData token) {
        return token.encrypt();
    }

    /**
     * Validates a refresh token against a stored hash.
     * @param rawToken the raw refresh token from the client
     * @param hashedToken the hashed refresh token from the database
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(NonSensitiveData rawToken, EncryptedData hashedToken) {
        return rawToken.matchesEncryptedString(hashedToken);
    }

    // ------ Helper methods ------ //

    /**
     * Gets the issue date of the refresh token.
     * This is the current date and time.
     * @return the issue date of the refresh token
     */
    public LocalDateTime getIssueDate() {
        return LocalDateTime.now();
    }

    /**
     * Gets the expiry date of the refresh token.
     * This is the current date and time plus 15 days.
     */
    public LocalDateTime issueExpiryDate() {
        return LocalDateTime.now().plusDays(DURATION_IN_DAYS);
    }



}
