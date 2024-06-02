package service;

import com.nimbusds.jose.JOSEException;
import exception.login.WrongEmailPasswordException;
import object.security.EncryptedData;
import object.security.NonSensitiveData;
import object.security.SensitiveData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.lang.Nullable;
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

    /** Token utility class for generating JWT tokens. */
    private static final TokenUtil jwtUtil = new TokenUtil();

    /** PostgresRequest instance. */
    private final PostgresRequest postgresRequest = new PostgresRequest();

    /** RefreshTokenService instance. */
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();

    /** Record the password and uid. */
    public record PassWordUIDPayload (@NotNull EncryptedData password, int uid) { }

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
    public Map<String, String> login(String email, SensitiveData password)
            throws SQLException, WrongEmailPasswordException, JOSEException {

        // Get the hashed password and uid from the database
        PassWordUIDPayload passWordUIDPayload = getHashedPassword(email);

        // If the email is not found, throw an exception
        if (passWordUIDPayload == null) throw new WrongEmailPasswordException();

        EncryptedData hashedPassword = passWordUIDPayload.password();
        int uid = passWordUIDPayload.uid();

        // Check if what the user entered matches the hashed password
        if (!password.matchesEncryptedString(hashedPassword)) {
            // throws an exception if the email or password is incorrect
            throw new WrongEmailPasswordException();
        } else {
            // get JWT token
            String token = jwtUtil.generateEncryptedToken(email, uid);

            // Generate a new refresh token
            NonSensitiveData refreshToken = refreshTokenService.generateRefreshToken();
            // Encrypt and save the encrypted refresh token to the database
            refreshTokenService.encryptAndSaveToken(refreshToken, uid);

            return Map.of("token", token, "refreshToken", refreshToken.toString());
        }
    }

    /**
     * Requests the hashed password from the database for a given email.
     * @return the hashed password or null if the email is not found
     * @throws SQLException if an error occurs while executing the SQL query
     */
    @Nullable
    private PassWordUIDPayload getHashedPassword(String email) throws SQLException {
        try (ResultSet resultSet = postgresRequest.executeQuery(SqlQueries.GET_USER_BY_EMAIL, email)) {
            if (resultSet.next()) {
                return new PassWordUIDPayload(
                        new EncryptedData(resultSet.getString("password")),
                        resultSet.getInt("uid")
                );
            } else {
                // return null if the email is not found
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error while getting hashed password", e);
            throw new SQLException("Error while getting hashed password", e);
        }
    }

}
