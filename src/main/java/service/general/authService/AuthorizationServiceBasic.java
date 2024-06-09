package service.general.authService;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.general.authService.jwtTokenService.JwtTokenService;


/**
 * The authorization service is responsible for authorizing requests.
 * For now, only JWT tokens are supported.
 */
@Service
public final class AuthorizationServiceBasic implements AuthorizationService {

    private final JwtTokenService jwtTokenService;

    @Value("${jwt.token.lifetime}")
    private Long jwtTokenLifetime;

    public AuthorizationServiceBasic(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Attempts to authorize a request, given a JWT token and a refresh token.
     * Will either return a new JWT token and refresh token, or an error message.
     *
     * @param jwtToken     The JWT token.
     * @param refreshToken The refresh token.
     * @param uid         The user ID.
     * @param email      The user's email.
     * @return The new tokens, if necessary.
     */
    @Override
    public TokenPayload authorizeRequest(String jwtToken, String refreshToken, int uid, String email) throws NotAuthorizedException, InternalServerError {
        // authorizes based on the jwt token
        try {
            if (jwtTokenService.verifyEncryptedToken(jwtToken, email, uid)) {
                return new TokenPayload(SuccessState.SUCCESS, null, null);
            } else {
                throw new NotAuthorizedException("Invalid JWT token.");
            }
        } catch (Exception e) {
            throw new InternalServerError("Failed to authorize request.");
        }
    }

    /**
     * Generates fresh tokens for a user.
     * This method is recommended to be called after a successful login, or when a user has registered.
     * The JWT token is encrypted using RSA, and the refresh token is stored in the database.
     * The JWT token is tied to the user's email and user ID. The refresh token is tied to the user's user ID.
     *
     * @param uid   The user ID.
     * @param email The user's email.
     * @return The new tokens.
     */
    @Override
    public TokenPayload generateFreshTokens(int uid, String email) throws InternalServerError {
        try {
            return new TokenPayload(SuccessState.SUCCESS, jwtTokenService.generateEncryptedToken(email, uid, jwtTokenLifetime), null);
        } catch (Exception e) {
            throw new InternalServerError("Failed to generate new tokens.");
        }
    }
}
