package service;


import object.security.EncryptedData;
import object.security.SensitiveData;
import request.PostgresRequest;
import exception.gen.RateLimitException;
import exception.gen.UserNotFoundException;
import exception.register.DuplicateEmailException;
import exception.register.ExpiredTokenException;
import exception.register.InvalidTokenException;
import exception.register.ValidationException;
import sql.SqlQueries;
import util.auth.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * @throws SQLException            if there is an error with the SQL query
     * @throws ValidationException     if the email, first name, last name, or password is invalid
     * @throws DuplicateEmailException if the email already exists in the database
     * @throws RateLimitException      if the rate limit is exceeded
     * @see ValidationUtil validation rules
     */
    public void createUser(String firstName, String lastName, String email, SensitiveData password, LocalDate dob)
            throws SQLException, ValidationException, DuplicateEmailException, RateLimitException {

        var validationUtil = new ValidationUtil();

        // Validate the email, first name, last name, and password
        validationUtil.validateEmail(email);
        validationUtil.validateName(firstName);
        validationUtil.validateName(lastName);
        // Validate the password
        password.validateData();

        EncryptedData encryptedPassword = password.encrypt();

        var dbRequest = new PostgresRequest();

        try (ResultSet resultSet = dbRequest.executeQuery(SqlQueries.CHECK_EMAIL_EXISTS, email)) {
            if (resultSet.next()) {
                logger.debug("Email already exists");
                throw new DuplicateEmailException("Email already exists");
            }
        }

        try (ResultSet resultSet = dbRequest.executeQuery(SqlQueries.ADD_USER, firstName, lastName, email, encryptedPassword, dob)){
            if (!resultSet.next()) {
                logger.error("User created but no UID returned");
                throw new SQLException("No UID returned");
            }
            storeEmailToken(resultSet.getInt("uid"));
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new DuplicateEmailException();
            } else {
                throw new SQLException();
            }
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
