package service.general.authService.refreshTokenService.dao;

import common.annotation.DatabaseOperation;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public interface RefreshTokenRepository {

    record RefreshToken(int uid, String refreshToken, LocalDateTime expDate) { }

    /**
     * Get the refresh tokens by the user id.
     *
     * @param uid The user id.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @DatabaseOperation
    List<RefreshToken> getRefreshTokensByUID(int uid)
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
    @DatabaseOperation
    int saveRefreshToken(int uid, String refreshToken)
    throws SQLException;

    /**
     * Delete ALL refresh tokens by the user id.
     *
     * @param uid The user id.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @DatabaseOperation
    int deleteRefreshTokenByUID(int uid)
    throws SQLException;

    /**
     * Delete a refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @DatabaseOperation
    int deleteRefreshToken(String refreshToken)
    throws SQLException;

}
