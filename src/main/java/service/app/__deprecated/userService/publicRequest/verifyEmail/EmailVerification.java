package service.app.__deprecated.userService.publicRequest.verifyEmail;

import common.config.SqlQueries;
import common.exception.gen.UserNotFoundException;
import common.exception.register.ExpiredTokenException;
import common.exception.register.InvalidTokenException;
import dto.request.PostgresRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.app.__deprecated.userService.publicRequest.register.RegistrationService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Interface to handle incoming email verification requests.
 */
public class EmailVerification {

    /** logger for the RegistrationService class */
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    /**
     * Verifies the email of a user.
     * Deletes the token from the database if the email is verified.
     *
     * @param emailToken the token sent to the user's email
     * @throws UserNotFoundException if the user is not found
     * @throws NullPointerException   if the token is not found
     * @throws ExpiredTokenException if the token has expired
     * @throws SQLException          if there is an error with the SQL query
     */
    public void verifyEmail(String emailToken)
            throws UserNotFoundException, InvalidTokenException, ExpiredTokenException, SQLException {
        var dbRequest = new PostgresRequest();
        try (var resultsSet = dbRequest.executeQuery(SqlQueries.DELETE_EMAIL_VERIFICATION_GET_UID_EXPIRY, emailToken)) {
            if (!resultsSet.next()) {
                throw new InvalidTokenException();
            }
            LocalDateTime expiry = LocalDateTime.now();
            if (resultsSet.getTimestamp("expiry").toLocalDateTime().isBefore(expiry)) {
                throw new ExpiredTokenException();
            } else {
                int uid = resultsSet.getInt("uid");
                int rowsUpdated = dbRequest.executeUpdate(SqlQueries.VERIFY_USER, uid);
                if (rowsUpdated == 0) {
                    logger.debug("Valid token but user not found for user {}", uid);
                    throw new UserNotFoundException("User not found");
                }
            }
        }
    }

    /**
     * Generates a unique token for email verification.
     * This method is used to generate a unique token which will be sent to the user's email for verification purposes.
     * The generated token should be stored and associated with the user's account for later verification.
     *
     * @return A unique token as a String.
     */
    private String generateEmailToken() {
        @SuppressWarnings("SpellCheckingInspection")
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return builder.toString();
    }


    /**
     * Stores the email token in the database.
     * The token is expired after 24 hours.
     * @param uid the UID of the user
     * @throws SQLException if there is an error with the SQL query
     */
    private void storeEmailToken(int uid)
            throws SQLException {
        var dbRequest = new PostgresRequest();

        LocalDateTime expiry = LocalDateTime.now().plusDays(1);

        dbRequest.executeUpdate(SqlQueries.ADD_EMAIL_VERIFICATION_TOKEN, generateEmailToken(), uid, expiry);
    }
}
