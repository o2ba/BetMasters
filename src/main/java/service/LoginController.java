package service;

import builder.PostgresRequest;
import exception.user.UserNotFoundException;
import exception.user.WrongEmailPasswordException;
import org.slf4j.Logger;
import util.EncryptionUtil;
import util.JwtUtil;
import sql.SqlQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;


/**
 * Controller class for handling login requests.
 * Using rotation of refresh tokens for persistent login.
 */
public class LoginController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoginController.class);

    private static final long EXPIRY_TIME_DEFAULT = 1000L * 60L * 15L; // 15 minutes
    private static final long EXPIRY_TIME_ROTATING_TOKEN = (long) 1000 * 60 * 60 * 24 * 30; // 30 days

    private static final EncryptionUtil encryptionUtil = new EncryptionUtil();
    private static final JwtUtil jwtUtil = new JwtUtil();

    public Map<String, String> login(String email, String password)
            throws WrongEmailPasswordException, SQLException {
        password = encryptionUtil.encrypt(password);
        PostgresRequest postgresRequest = new PostgresRequest();

        try (ResultSet resultSet = postgresRequest.executeQuery(SqlQueries.GET_USER_BY_EMAIL_AND_PASSWORD, email, password)) {
            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                String jwt = jwtUtil.buildToken(email, uid, EXPIRY_TIME_DEFAULT);

                String refreshToken = UUID.randomUUID().toString();
                long refreshTokenExpiryIn = System.currentTimeMillis() / 1000L + EXPIRY_TIME_ROTATING_TOKEN;

                postgresRequest.executeUpdate(SqlQueries.STORE_REFRESH_TOKEN, refreshToken, uid, refreshTokenExpiryIn);

                return Map.of("jwt", jwt, "refreshToken", refreshToken);
            } else {
                throw new WrongEmailPasswordException();
            }
        } catch (SQLException e) {
            logger.error("Error occurred while logging in: {}", e.getMessage());
            throw new SQLException();
        }
    }

    public Map<String, String> refresh(String oldRefreshToken)
            throws SQLException {
        PostgresRequest postgresRequest = new PostgresRequest();

        try (ResultSet resultSet = postgresRequest.executeQuery(SqlQueries.GET_USER_BY_REFRESH_TOKEN, oldRefreshToken)) {
            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                String email = resultSet.getString("email");

                // Generate a new JWT
                String jwt = jwtUtil.buildToken(email, uid, EXPIRY_TIME_DEFAULT);

                // Generate a new refresh token
                String newRefreshToken = UUID.randomUUID().toString();
                long refreshTokenExpiryIn = System.currentTimeMillis() / 1000L + EXPIRY_TIME_ROTATING_TOKEN;

                // Store the new refresh token in your database
                postgresRequest.executeUpdate(SqlQueries.STORE_REFRESH_TOKEN, newRefreshToken, uid, refreshTokenExpiryIn);

                // Invalidate the old refresh token
                postgresRequest.executeUpdate(SqlQueries.INVALIDATE_REFRESH_TOKEN, oldRefreshToken);

                // Return the new JWT and the new refresh token
                return Map.of("jwt", jwt, "refreshToken", newRefreshToken);
            } else {
                throw new SQLException("Invalid refresh token");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while refreshing token: " + e.getMessage());
            throw new SQLException();
        }
    }
}
