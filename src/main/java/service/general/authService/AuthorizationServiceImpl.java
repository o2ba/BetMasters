package service.general.authService;

import com.nimbusds.jose.JOSEException;
import common.annotation.DatabaseOperation;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.general.authService.jwtTokenService.JwtTokenService;
import service.general.authService.refreshTokenService.RefreshTokenService;

import java.sql.SQLException;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    @Value("${jwt.token.lifetime}")
    private Long jwtTokenLifetime;

    @Value("${refresh.token.lifetime}")
    private Long refreshTokenLifetime;

    public AuthorizationServiceImpl(JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService) {
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Attempts to authorize a request, given a JWT token and a refresh token.
     * Will either return a new JWT token and refresh token, or an error message.
     *
     * @param jwtToken     The JWT token.
     * @param refreshToken The refresh token.
     * @return The new tokens, if necessary.
     */
    @Override
    @DatabaseOperation
    public TokenPayload authorizeRequest(String jwtToken, String refreshToken, int uid, String email)
    throws NotAuthorizedException, InternalServerError {
        try {
            // Check if the JWT token is valid. If yes, return SUCCESS.
            if (jwtTokenService.verifyEncryptedToken(jwtToken, email, uid)) {
                return new TokenPayload(SuccessState.SUCCESS, null, null);
            } else {
                // Check the state of the refresh token.
                switch (refreshTokenService.getRefreshTokenAgeGroup(refreshToken)) {
                    // The token is less than half its lifetime. Issue a new JWT token.
                    case MINOR -> {
                        return new TokenPayload(SuccessState.NEW_TOKENS_REQUIRED,
                                jwtTokenService.generateEncryptedToken(email, uid, jwtTokenLifetime), refreshToken);
                    }
                    // The token is more than half its lifetime. Issue a new JWT token and refresh token.
                    case ADULT -> {
                        refreshTokenService.revokeRefreshToken(refreshToken);
                        String newRefreshToken = refreshTokenService.issueRefreshToken(uid, refreshTokenLifetime);
                        return new TokenPayload(SuccessState.NEW_TOKENS_REQUIRED,
                                jwtTokenService.generateEncryptedToken(email, uid, jwtTokenLifetime), newRefreshToken);
                    }
                    // The token is no longer valid. Throw an exception.
                    case DEAD -> throw new NotAuthorizedException("User not authorized.");
                    // This should never happen.
                    default -> throw new InternalServerError("Internal server error.");
                }
            }
        } catch (SQLException | JOSEException e) {
            throw new InternalServerError("Internal server error.");
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
    @DatabaseOperation
    public TokenPayload generateFreshTokens(int uid, String email) throws InternalServerError {
        try {
            // Generate a new JWT token.
            String jwtToken = jwtTokenService.generateEncryptedToken(email, uid, jwtTokenLifetime);
            // Issue a new refresh token, and store it in the database.
            String refreshToken = refreshTokenService.issueRefreshToken(uid, refreshTokenLifetime);
            // Return the new tokens.
            return new TokenPayload(SuccessState.NEW_TOKENS_REQUIRED, jwtToken, refreshToken);
        } catch (SQLException | JOSEException e) {
            throw new InternalServerError("Internal server error.");
        }
    }


}
