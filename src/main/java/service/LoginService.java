package service;

import exception.login.WrongEmailPasswordException;
import object.security.EncryptedData;
import object.security.SensitiveData;
import org.slf4j.Logger;
import request.PostgresRequest;
import sql.SqlQueries;
import util.auth.TokenUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


/**
 * Controller class for handling login requests.
 * Using rotation of refresh tokens for persistent login.
 */
public class LoginService {

    /** Logger for the LoginService class. */
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoginService.class);

    /** Default expiry time for a token. */
    private static final long EXPIRY_TIME_DEFAULT = 1000L * 60L * 15L; // 15 minutes

    /** Token utility class for generating JWT tokens. */
    private static final TokenUtil jwtUtil = new TokenUtil();


    /**
     * Logs in a user.
     * This method first encrypts the password and then checks if the email and password match.
     * Then, a token and a refresh token are generated and returned.
     * @param email the email of the user
     * @param password the password of the user
     * @return a map containing the token and refresh token
     * @throws SQLException if an error occurs while executing the SQL query
     * @throws WrongEmailPasswordException if the email or password is incorrect
     */
    public Map<String, String> login(String email, SensitiveData password) throws SQLException, WrongEmailPasswordException {
        PostgresRequest postgresRequest = new PostgresRequest();
        try (ResultSet resultSet = postgresRequest.executeQuery(SqlQueries.GET_USER_BY_EMAIL, email)) {
            if (resultSet.next()) {
                EncryptedData dbPassword = new EncryptedData(resultSet.getString("password"));
                if (password.matchesEncryptedString(dbPassword)) {
                    // Generates a token valid for 15 minutes
                    String token = jwtUtil.buildToken(email, resultSet.getInt("uid"), EXPIRY_TIME_DEFAULT);

                    // String refreshToken = generateRefreshToken();
                    return Map.of("token", token, "refreshToken", "refreshToken");
                } else {
                    // Wrong password
                    throw new WrongEmailPasswordException();
                }
            } else {
                // Unknown email
                throw new WrongEmailPasswordException();
            }
        } catch (SQLException e) {
            logger.error("Error while logging in user", e);
            throw new SQLException("Error while logging in user", e);
        } catch (Exception e) {
            logger.error("Error while logging in user", e);
            throw new RuntimeException("Error while logging in user", e);
        }

    }


}
