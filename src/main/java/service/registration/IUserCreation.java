package service.registration;

import common.annotation.DatabaseOperation;
import common.object.security.SensitiveData;
import common.util.auth.UserUtil;
import common.util.auth.ValidationUtil;
import dto.request.PostgresRequest;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Handles the logic for creating a user.
 */
public interface IUserCreation {

    /** @see ValidationUtil */
    ValidationUtil validationUtil = new ValidationUtil();

    /** @see PostgresRequest */
    PostgresRequest postgresRequest = new PostgresRequest();

    /** @see org.slf4j.Logger */
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(IUserCreation.class);

    /** @see UserUtil */
    UserUtil userUtil = new UserUtil();

    /** SQL Queries **/
    enum SQLQueries {
        CHECK_EMAIL_UNIQUE("SELECT email FROM users WHERE email = ?"),
        CREATE_USER("INSERT INTO users (first_name, last_name, email, password, dob) VALUES (?, ?, ?, ?, ?) RETURNING uid");

        private final String query;

        SQLQueries(String query) {
            this.query = query;
        }

        @Override
        public String toString() {
            return query;
        }
    }

    /**
     * Validates the input for creating a user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param dob The date of birth of the user.
     * @return True if the input is valid, false otherwise.
     */
    boolean isUserInputValid(String firstName, String lastName, String email, SensitiveData password, LocalDate dob);

    /**
     * Checks if the user is of legal age.
     * This will depend on the country of residence. For now, the assumption is that the user must be 18 years old.
     * @param dob The date of birth of the user.
     */
    boolean userOfLegalAge(LocalDate dob);

    /**
     * Checks if the email is unique.
     * @param email The email to check.
     * @return True if the email is unique, false otherwise.
     */
    @DatabaseOperation
    boolean isEmailUnique(String email)
    throws SQLException;


    /**
     * Creates a new user. The password is hashed before being stored in the database.
     * @return The unique identifier of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param dob The date of birth of the user.
     */
    @DatabaseOperation
    int createUser(String firstName, String lastName, String email, SensitiveData password, LocalDate dob)
    throws SQLException;

}
