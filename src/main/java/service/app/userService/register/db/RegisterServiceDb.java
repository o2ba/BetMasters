package service.app.userService.register.db;

import common.exception.UnhandledErrorException;
import common.exception.gen.TimeoutException;
import common.security.SensitiveData;
import org.springframework.stereotype.Component;
import service.app.userService.register.exception.DuplicateEmailException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public interface RegisterServiceDb {

    /**
     * Adds a user to the database
     * @param first_name The first name of the user
     * @param last_name The last name of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param dob The date of birth of the user
     * @param createdAt The date and time the user was created
     * @return The user id of the user
     * @throws UnhandledErrorException The exception is thrown when an error occurs that is not handled by the system
     * @throws DuplicateEmailException The exception is thrown when the email already exists in the database
     * @throws TimeoutException The exception is thrown when the database operation times out
     */
    int addUser(String first_name,
                String last_name,
                String email,
                SensitiveData password,
                LocalDate dob,
                LocalDateTime createdAt)
    throws UnhandledErrorException, DuplicateEmailException, TimeoutException;
}