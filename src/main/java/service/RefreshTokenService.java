package service;

import object.security.NonSensitiveData;
import org.jetbrains.annotations.Nullable;
import request.PostgresRequest;
import sql.SqlQueries;
import util.auth.RefreshTokenUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service class for handling refresh tokens.
 * When a user logs in, all past refresh tokens are deleted and a new one is created.
 * When a user makes a request that requires authentication, if their refresh token is more than 5 days old,
 * a new one is generated and the old one is deleted.
 */
public class RefreshTokenService {

    /**
     * The RefreshTokenUtil instance.
     */
    private final RefreshTokenUtil refreshTokenUtil;


    /**
     * The PostgresRequest instance.
     */
    private final PostgresRequest postgresRequest;


    public RefreshTokenService() {
        this.refreshTokenUtil = new RefreshTokenUtil();
        this.postgresRequest = new PostgresRequest();
    }

    /**
     * Generates a new refresh token for a user.
     * @return the refresh token
     * @see RefreshTokenUtil#generateToken()
     **/
    public NonSensitiveData generateRefreshToken() {
        return refreshTokenUtil.generateToken();
    }

    /**
     * Encrypts the refresh token and saves it to the database.
     * @param refreshToken the refresh token
     */
    public void encryptAndSaveToken(NonSensitiveData refreshToken, int uid) throws SQLException {
        postgresRequest.executeUpdate(
                SqlQueries.ADD_REFRESH_TOKEN,
                refreshToken.encrypt().toString(),
                uid,
                refreshTokenUtil.getIssueDate(),
                refreshTokenUtil.getExpiryDate()
        );
    }


    /**
     * Gets the refresh token from the database for a given user id.
     * @param uid the user id
     * @return the refresh token or null if the user has no refresh token
     */
    public @Nullable String getValidRefreshToken(int uid) throws SQLException {
        try(ResultSet resultSet = postgresRequest.executeQuery(SqlQueries.GET_REFRESH_TOKEN, uid)) {
            if (resultSet.next()) {
                return resultSet.getString("token");
            } else {
                return null;
            }
        }
    }

}
