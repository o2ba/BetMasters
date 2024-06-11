package service.app.noAuthRequestService.register;

import common.exception.InternalServerError;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.model.security.SensitiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.noAuthRequestService.register.dao.RegisterServiceDao;
import service.general.validation.Validation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RegisterServiceImpl implements RegisterService {

    RegisterServiceDao registerServiceDao;
    Validation validation;

    @Autowired
    public RegisterServiceImpl(RegisterServiceDao registerServiceDao, Validation validation) {
        this.registerServiceDao = registerServiceDao;
        this.validation = validation;
    }

    /**
     * Adds a user to the database. The data is first validated before adding the user,
     * then, a query is executed to add the user to the database; Then a verification email is sent to the user.
     *
     * @param first_name The first name of the user
     * @param last_name  The last name of the user
     * @param email      The email of the user
     * @param password   The password of the user
     * @param dob        The date of birth of the user
     * @throws DuplicateEmailException If the email already exists
     * @throws InternalServerError     If an error occurs while adding the user
     * @throws ValidationException     If the data is not valid
     */
    @Override
    public void addUser(String first_name, String last_name, String email, SensitiveData password, LocalDate dob)
    throws DuplicateEmailException, InternalServerError, ValidationException {

        if(!validation.isNameValid(first_name) || !validation.isNameValid(last_name)) {
            throw new ValidationException(ValidationException.ValidationFailure.NAME);
        } else if(!validation.isEmailValid(email)) {
            throw new ValidationException(ValidationException.ValidationFailure.EMAIL);
        } else if(!validation.isPasswordValid(password)) {
            throw new ValidationException(ValidationException.ValidationFailure.PASSWORD);
        } else if(!validation.isDobValid(dob)) {
            throw new ValidationException(ValidationException.ValidationFailure.DOB);
        }

        int result = registerServiceDao.addUser(first_name, last_name, email, password, dob, LocalDateTime.now());

        if (result == 0) {
            throw new InternalServerError("Error while adding user");
        }
    }

}
