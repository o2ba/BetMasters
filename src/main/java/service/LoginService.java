package service;

import com.nimbusds.jose.JOSEException;
import exception.login.WrongEmailPasswordException;
import object.security.EncryptedData;
import object.security.NonSensitiveData;
import object.security.SensitiveData;
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

    /** Default expiry time for a token. */
    private static final long EXPIRY_TIME_DEFAULT = 1000L * 60L * 60L; // 60 minutes

    /** Token utility class for generating JWT tokens. */
    private static final TokenUtil jwtUtil = new TokenUtil();

    /** PostgresRequest instance. */
    private final PostgresRequest postgresRequest = new PostgresRequest();

    /** RefreshTokenService instance. */
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();


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
        EncryptedData hashedPassword = getHashedPassword(email);
        if (hashedPassword == null || !password.matchesEncryptedString(hashedPassword)) {
            // throws an exception if the email or password is incorrect
            throw new WrongEmailPasswordException();
        } else {
            // request JWT token
            // String token = jwtUtil.buildToken(email, EXPIRY_TIME_DEFAULT);
            
            // Generate a new refresh token
            NonSensitiveData refreshToken = refreshTokenService.generateRefreshToken();
            // Encrypt and save the refresh token
            // refreshTokenService.encryptAndSaveToken(refreshToken, email);


        }

        return null;
    }

    /**
     * Requests the hashed password from the database for a given email.
     * @return the hashed password or null if the email is not found
     * @throws SQLException if an error occurs while executing the SQL query
     */
    @Nullable
    private EncryptedData getHashedPassword(String email) throws SQLException {
        try (ResultSet resultSet = postgresRequest.executeQuery(SqlQueries.GET_USER_BY_EMAIL, email)) {
            if (resultSet.next()) {
                return new EncryptedData(resultSet.getString("password"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error while getting hashed password", e);
            throw new SQLException("Error while getting hashed password", e);
        }
    }




}
