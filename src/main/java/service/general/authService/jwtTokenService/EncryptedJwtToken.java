package service.general.authService.jwtTokenService;

import com.nimbusds.jose.JOSEException;
import org.jetbrains.annotations.NotNull;

public interface EncryptedJwtToken {

    /**
     * Generates an RSA-Encrypted JWT token for the given subject and user ID.
     *
     * @param subject  the subject of the token
     * @param uid      the user ID
     * @param lifetime the lifetime of the token in milliseconds
     * @return the generated token
     */
    @NotNull
    String generateEncryptedToken(String subject, int uid, long lifetime) throws JOSEException;

    /**
     * Checks if a token corresponds to a user.
     * @param token the token to verify
     * @param subject the subject to verify (usually email)
     * @param uid the user ID
     * @return true if the token is valid, false otherwise
     */
    boolean verifyEncryptedToken(String token, String subject, int uid);


}
