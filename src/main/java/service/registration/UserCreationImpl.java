package service.registration;

import common.annotation.DatabaseOperation;
import common.exception.register.ValidationException;
import common.object.security.SensitiveData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserCreationImpl implements IUserCreation {

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
        try {
            validationUtil.validateName(firstName);
            validationUtil.validateName(lastName);
            validationUtil.validateEmail(email);
            password.validateData();
        } catch (ValidationException e) {
            return false;
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
