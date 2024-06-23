package service.app.authRequestService.authService.refreshTokenService.dao;

import common.annotation.DatabaseOperation;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface RefreshTokenDao {

    record RefreshToken(int uid, String refreshToken, LocalDateTime expDate, LocalDateTime halfDate) { }

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
     * Get the refresh token by the refresh token ID
     * @param refreshToken The refresh token.
     */
    @DatabaseOperation
    RefreshToken getRefreshToken(String refreshToken);


    /**
     * Save the refresh token.
     * Depending on the implementation, this may have to delete the v1 refresh token.
     *
     * @param uid The user id.
     * @param refreshToken The refresh token.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @DatabaseOperation
    int saveRefreshToken(int uid, String refreshToken, LocalDateTime expDate, LocalDateTime halfExpDate)
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
