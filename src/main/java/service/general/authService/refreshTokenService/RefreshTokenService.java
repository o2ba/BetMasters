package service.general.authService.refreshTokenService;

import common.annotation.DatabaseOperation;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public interface RefreshTokenService {


    /**
     * Indicates the age of a refresh token.
     * A refresh token can be either NEWBORN, SENIOR or DEAD.
     * MINOR refresh tokens are newly issued, and have can be used to issue new JWT tokens.
     * ADULT refresh tokens will generate a new MINOR refresh token when used to issue a new JWT token.
     * DEAD refresh tokens are no longer valid.
     */
    enum RefreshTokenAgeGroup {
        MINOR,
        ADULT,
        DEAD
    }

    /**
     * Issue a new refresh token.
     * This includes generating a new refresh token and saving it to the database.
     *
     * @return The new refresh token.
     * @throws SQLException If the operation fails.
     */
    @DatabaseOperation
    String issueRefreshToken(int uid, Long lifetime)
    throws SQLException;

    /**
     * Revoke a refresh token.
     * This includes deleting the refresh token from the database.
     *
     * @param refreshToken The refresh token to revoke.
     * @throws SQLException If the operation fails.
     */
    @DatabaseOperation
    void revokeRefreshToken(String refreshToken)
    throws SQLException;

    /**
     * Gets the age group of a refresh token.
     * Recommendation on how to handle respective refresh tokens:
     * MINOR: A new JWT token can be issued if necessary.
     * ADULT: A new JWT token can be issued if necessary.
     * A new refresh token should be issued. A refresh token is considered ADULT if it is more than half its lifetime.
     * DEAD: The refresh token is no longer valid. The user should be logged out.
     *
     * @param refreshToken The refresh token.
     * @return The age group of the refresh token.
     */
    RefreshTokenAgeGroup getRefreshTokenAgeGroup(String refreshToken);

}
