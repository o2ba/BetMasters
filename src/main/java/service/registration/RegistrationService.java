package service.registration;


import common.annotation.RateLimited;
import common.object.security.SensitiveData;
import common.exception.gen.RateLimitException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.util.auth.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Allows the creation and deletion of users, as well as the verification of email addresses.
 */
public final class RegistrationService {

    /** UserCreationImpl object */
    private final UserCreationImpl userCreationImpl = new UserCreationImpl();

    /**
     * Creates a new user.
     * Automatically sends an email verification token to the user.
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
    @RateLimited
    public void createUser(String firstName, String lastName, String email, SensitiveData password, LocalDate dob)
            throws SQLException, ValidationException, DuplicateEmailException, RateLimitException {
        if (!userCreationImpl.isUserInputValid(firstName, lastName, email, password, dob)) {
            throw new ValidationException("Invalid input");
        } else if (!userCreationImpl.isEmailUnique(email)) {
            throw new DuplicateEmailException();
        } else if (userCreationImpl.userOfLegalAge(dob)) {
            int uid = userCreationImpl.createUser(firstName, lastName, email, password, dob);
            // todo call email service to send email verification token
        }

    }

}
