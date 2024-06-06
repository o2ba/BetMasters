package service.general.authService.refreshTokenService.dao;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public interface RefreshTokenRepository {

    /**
     * Get the refresh token by the user id.
     *
     * @param uid The user id.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @
    ResultSet getRefreshTokenByUID(int uid)
    throws SQLException;

    /**
     * Save the refresh token.
     * Depending on the implementation, this may have to delete the old refresh token.
     *
     * @param uid The user id.
     * @param refreshToken The refresh token.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    int saveRefreshToken(int uid, String refreshToken)
    throws SQLException;
}
