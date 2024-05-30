package main.java.service;


import main.java.builder.PostgresRequest;
import main.java.exception.sql.UserNotFoundException;
import main.java.exception.sql.ValidationException;
import main.java.sql.SqlQueries;
import main.java.util.EncryptionUtil;
import main.java.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Random;

/**
 * Allows the creation and deletion of users, as well as the verification of email addresses.
 */
public final class RegistrationService {
    /**
     * logger for the RegistrationService class
     */
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);


    /**
     * Creates a new user. The password is hashed before being stored in the database.
     * All information is validated before being stored.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param password  the password of the user
     * @param dob       the date of birth of the user
     */
    public void createUser(String firstName, String lastName, String email, String password, String dob)
            throws SQLException, ValidationException {
        var encryptionUtil = new EncryptionUtil();
        password = encryptionUtil.encrypt(password);

        var validationUtil = new ValidationUtil();
        validationUtil.validateEmail(email);
        validationUtil.validateName(firstName);
        validationUtil.validateName(lastName);
        validationUtil.validatePassword(password);

        var dbRequest = new PostgresRequest();
        try {
            dbRequest.executeUpdate(SqlQueries.ADD_USER, email, password);
            storeEmailToken(email);
        } catch (SQLException e) {
            logger.error("Error creating user", e);
            throw new SQLException("Error creating user");
        }
    }

    /**
     * Deletes a user.
     *
     * @param uid the UID of the user
     */
    public void deleteUser(int uid)
            throws UserNotFoundException, SQLException {
        var dbRequest = new PostgresRequest();
        int rowsUpdated = dbRequest.executeUpdate(SqlQueries.DELETE_USER, uid);
        if (rowsUpdated == 0) {
            logger.debug("User not found for user {}", uid);
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Verifies the email of a user.
     * Deletes the token from the database if the email is verified.
     *
     * @param emailToken the token sent to the user's email
     * @throws UserNotFoundException if the user is not found
     * @throws SQLException          if there is an error with the SQL query
     */
    public void verifyEmail(String emailToken)
            throws UserNotFoundException, SQLException {
        var dbRequest = new PostgresRequest();
        try (var resultsSet = dbRequest.executeQuery(SqlQueries.DELETE_EMAIL_VERIFICATION_GET_UID_EXPIRY, emailToken)) {
            if (!resultsSet.next()) {
                logger.debug("Invalid token");
            }
            int uid = resultsSet.getInt("uid");
            logger.debug("User found with uid: {}", uid);

            int rowsUpdated = dbRequest.executeUpdate(SqlQueries.VERIFY_USER, uid);
            if (rowsUpdated == 0) {
                logger.debug("Valid token but user not found for user {}", uid);
                throw new UserNotFoundException("User not found");
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

    private void storeEmailToken(String emailToken)
            throws SQLException {
        var dbRequest = new PostgresRequest();
        dbRequest.executeUpdate(SqlQueries.ADD_EMAIL_VERIFICATION_TOKEN, generateEmailToken(), emailToken);
    }
}
