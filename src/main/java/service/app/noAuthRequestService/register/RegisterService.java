package service.app.noAuthRequestService.register;

import common.exception.InternalServerError;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.model.security.SensitiveData;

import java.time.LocalDate;

public interface RegisterService {
    /**
     * Adds a user to the database. The data is first validated before adding the user,
     * then, a query is executed to add the user to the database; Then a verification email is sent to the user.
     * @param first_name The first name of the user
     * @param last_name The last name of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param dob The date of birth of the user
     * @throws DuplicateEmailException If the email already exists
     * @throws InternalServerError If an error occurs while adding the user
     * @throws ValidationException If the data is not valid
     */
    void addUser(String first_name, String last_name, String email, SensitiveData password, LocalDate dob)
    throws DuplicateEmailException, InternalServerError, ValidationException;
}
