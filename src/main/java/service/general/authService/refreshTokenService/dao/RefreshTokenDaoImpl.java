package service.general.authService.refreshTokenService.dao;

import dto.request.PostgresRequest;
import service.general.authService.refreshTokenService.token.RefreshTokenGenerator;
import service.general.authService.refreshTokenService.token.RefreshTokenGeneratorImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RefreshTokenDaoImpl implements RefreshTokenDao {


    PostgresRequest postgresRequest;

    public RefreshTokenDaoImpl(PostgresRequest postgresRequest) {
        this.postgresRequest = postgresRequest;
    }

    /**
     * Get the refresh tokens by the user id.
     *
     * @param uid The user id.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @Override
    public List<RefreshToken> getRefreshTokensByUID(int uid) throws SQLException {
        try (ResultSet qu = postgresRequest.executeQuery(refreshTokenSqlQueries.GET_REFRESH_TOKENS_BY_UID.getQuery(),
                uid
                )) {
            // convert the result set to a list of refresh tokens
            List<RefreshToken> refreshTokens = new ArrayList<>();
            // loop through the result set

            while (qu.next()) {
                // get the refresh token
                String refreshToken = qu.getString("token");
                // get the expiry date
                LocalDateTime expDate = qu.getTimestamp("expiry_date").toLocalDateTime();
                // get the half expiry date
                LocalDateTime halfExpDate = qu.getTimestamp("halftime_date").toLocalDateTime();
                // create a new refresh token object
                RefreshToken token = new RefreshToken(uid, refreshToken, expDate, halfExpDate);
                // add the refresh token to the list
                refreshTokens.add(token);
            }

            return refreshTokens;


        } catch (SQLException e) {
            throw new SQLException("Failed to get refresh tokens by user id");
        }
    }

    /**
     * Get the refresh token by the refresh token ID
     *
     * @param refreshToken The refresh token.
     */
    @Override
    public RefreshToken getRefreshToken(String refreshToken) {
        return null;
    }

    /**
     * Save the refresh token.
     * Depending on the implementation, this may have to delete the old refresh token.
     *
     * @param uid          The user id.
     * @param refreshToken The refresh token.
     * @param expDate
     * @param halfExpDate
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @Override
    public int saveRefreshToken(int uid, String refreshToken, LocalDateTime expDate, LocalDateTime halfExpDate) throws SQLException {
        try {
            return postgresRequest.executeUpdate(refreshTokenSqlQueries.SAVE_REFRESH_TOKEN.getQuery(), uid, refreshToken, expDate, halfExpDate);
        } catch (SQLException e) {
            throw new SQLException("Failed to save refresh token");
        }
    }

    /**
     * Delete ALL refresh tokens by the user id.
     *
     * @param uid The user id.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @Override
    public int deleteRefreshTokenByUID(int uid) throws SQLException {
        return 0;
    }

    /**
     * Delete a refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The result set.
     * @throws SQLException If the query fails.
     */
    @Override
    public int deleteRefreshToken(String refreshToken) throws SQLException {
        return 0;
    }
}
