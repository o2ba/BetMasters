package service.app.user.account.register;

import com.google.gson.JsonObject;
import common.exception.UnhandledErrorException;
import common.security.SensitiveData;
import service.app.user.account.register.exception.DuplicateEmailException;
import service.app.user.account.register.exception.InvalidAgeException;
import service.app.user.account.register.exception.InvalidInputException;
import java.time.LocalDate;

/**
 * Interface defining the contract for user registration operations.
 */
public interface RegisterService {

    /**
     * Registers a new user with the provided details.
     *
     * @param first_name The first name of the user.
     * @param last_name The last name of the user.
     * @param email The email address of the user.
     * @param password The sensitive data (password) of the user.
     * @param dob The date of birth of the user.
     * @return A JsonObject containing registration information, including a JWT token, UID, and email.
     * @throws DuplicateEmailException If the provided email address already exists in the system.
     * @throws InvalidAgeException If the age derived from the provided date of birth is invalid.
     * @throws InvalidInputException If any of the input parameters (name, email, password, dob) are invalid.
     * @throws UnhandledErrorException If an unexpected error occurs during user registration.
     */
    JsonObject addUser(String first_name, String last_name, String email, SensitiveData password, LocalDate dob)
            throws DuplicateEmailException, InvalidAgeException, InvalidInputException, UnhandledErrorException;
}
