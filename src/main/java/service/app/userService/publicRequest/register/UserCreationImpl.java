package service.app.userService.publicRequest.register;

import common.annotation.DatabaseOperation;
import common.object.security.SensitiveData;
import service.general.__deprecated.user.UserUtil;
import service.general.__deprecated.user.interfaces.InputValidation;
import dto.request.PostgresRequest;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class UserCreationImpl implements UserCreation {

    private final InputValidation validationUtil;
    private final UserUtil userUtil;
    private final Logger logger;
    private final PostgresRequest postgresRequest;

    /**
     * Passes the necessary objects to the UserCreationImpl object.
     * @see InputValidation
     * @see UserUtil
     * @see Logger
     * @see PostgresRequest
     */
    public UserCreationImpl(InputValidation validationUtil, UserUtil userUtil, Logger logger, PostgresRequest postgresRequest) {
        this.validationUtil = validationUtil;
        this.userUtil = userUtil;
        this.logger = logger;
        this.postgresRequest = postgresRequest;
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
    @Override
    public boolean isUserInputValid(String firstName, String lastName, String email, SensitiveData password, LocalDate dob) {
        if (validationUtil.isEmailValid(email)
                && validationUtil.isNameValid(firstName)
                && validationUtil.isNameValid(lastName)
                && validationUtil.isDobValid(dob)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the email is unique.
     * @param email The email to check.
     * @return True if the email is unique, false otherwise.
     * @throws SQLException If the query fails.
     */
    @Override
    @DatabaseOperation
    public boolean isEmailUnique(String email)
    throws SQLException {
        try (ResultSet resultSet = postgresRequest.executeQuery(SQLQueries.CHECK_EMAIL_UNIQUE.toString(),
                email)) {
            if (resultSet.next()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the user is of legal age.
     * @param dob The date of birth of the user.
     * @return True if the user is of legal age, false otherwise.
     */
    @Override
    public boolean userOfLegalAge(LocalDate dob) {
        return userUtil.getAge(dob) >= 18;
    }

    /**
     * Creates a new user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param dob The date of birth of the user.
     * @return The UID of the user.
     * @throws SQLException If the query fails.
     */
    @Override
    @DatabaseOperation
    public int createUser(String firstName, String lastName, String email, SensitiveData password, LocalDate dob)
    throws SQLException {

        int uid;

        try (ResultSet resultSet = postgresRequest.executeQuery(SQLQueries.CREATE_USER.toString(),
                firstName, lastName, email, password.encrypt(), dob)) {
            if (!resultSet.next()) {
                logger.error("User created but no UID returned");
                throw new SQLException("No UID returned");
            } else {
                uid = resultSet.getInt("uid");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new SQLException("Duplicate email");
            } else {
                throw new SQLException();
            }
        }
        return uid;
    }
}
