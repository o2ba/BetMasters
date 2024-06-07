package service.general.authService;


import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;

/**
 * Main interface for authorization service.
 * Handles all authorization related operations, such as login, logout, etc.
 *
 */
public interface AuthorizationService {

    /**
     * The possible states of the authorization process.
     */
    enum SuccessState {
        // The user was authorized successfully. No new tokens are required.
        SUCCESS,
        // The user has been authorized, but the token has expired.
        NEW_TOKENS_REQUIRED
    }

    /**
     * The token payload sent to the user, if necessary.
     */
    record TokenPayload(SuccessState state, String jwtToken, String refreshToken) { }

    /**
     * Attempts to authorize a request, given a JWT token and a refresh token.
     * Will either return a new JWT token and refresh token, or an error message.
     *
     * @param jwtToken The JWT token.
     * @param refreshToken The refresh token.
     * @return The new tokens, if necessary.
     */
    TokenPayload authorizeRequest(String jwtToken, String refreshToken, int uid, String email)
    throws NotAuthorizedException, InternalServerError;


    /**
     * Generates fresh tokens for a user.
     * This method is recommended to be called after a successful login, or when a user has registered.
     * The JWT token is encrypted using RSA, and the refresh token is stored in the database.
     * The JWT token is tied to the user's email and user ID. The refresh token is tied to the user's user ID.
     *
     * @param uid The user ID.
     * @param email The user's email.
     * @return The new tokens.
     */
    TokenPayload generateFreshTokens(int uid, String email)
    throws InternalServerError;

}
