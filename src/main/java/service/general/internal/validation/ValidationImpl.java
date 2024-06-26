package service.general.internal.validation;

import common.security.SensitiveData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidationImpl implements Validation {

    /**
     * <p>Checks if the name is valid.</p>
     * <p><b>Validation Criteria:</b></p>
     * <ul>
     *     <li>Must not be null</li>
     *     <li>Must not be empty</li>
     *     <li>Must only contain a-z, A-Z, and space</li>
     *     <li>Must be longer than 2 characters, but shorter than 50</li>
     * </ul>
     *
     * @param name The name to be validated
     * @return True if the name is valid, false otherwise
     */
    @Override
    public boolean isNameValid(String name) {
        return (name.length() >= 2 && name.length() <= 50 && name.matches("^[a-zA-Z-'\\s]+$"));
    }

    /**
     * <p>Checks if the email is valid.</p>
     * <p><b>Validation Criteria:</b></p>
     * <ul>
     *     <li>Must not be null</li>
     *     <li>Must not be empty</li>
     *     <li>Must be shorter than 254 characters</li>
     *     <li>Must be a valid email (@ exists, and . after the @)</li>
     * </ul>
     *
     * @param email The email to be validated
     * @return True if the email is valid, false otherwise
     */
    @Override
    public boolean isEmailValid(String email) {
        if (!email.matches("^.+@.+\\..+$")) {
            return false;
        } else return email.length() <= 254;
    }

    /**
     * <p>Checks if the password is valid.</p>
     * <p><b>Validation Criteria:</b></p>
     * <ul>
     *     <li>Must not be null</li>
     *     <li>Must not be empty</li>
     *     <li>Must be longer than 8 characters, but shorter than 50</li>
     *     <li>Must contain at least one uppercase letter, one lowercase letter, one number, and one special character</li>
     * </ul>
     *
     * @param password The password to be validated
     * @return True if the password is valid, false otherwise
     */
    @Override
    public boolean isPasswordValid(SensitiveData password) {
        return password.isValidData();
    }

    /**
     * <p>Checks if the date of birth is valid.</p>
     * <p><b>Validation Criteria:</b></p>
     * <ul>
     *     <li>Must not be null</li>
     *     <li>Must not be empty</li>
     *     <li>Must be in the format yyyy-MM-dd</li>
     *     <li>Must be a valid date</li>
     *     <li>Must be in the past</li>
     * </ul>
     *
     * @param date The date to be validated
     * @return True if the date is valid, false otherwise
     */
    @Override
    public boolean isDobValid(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
}