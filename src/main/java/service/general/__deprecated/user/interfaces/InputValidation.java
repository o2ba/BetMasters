package service.general.__deprecated.user.interfaces;


import java.time.LocalDate;


/**
 * The InputValidation interface provides a contract for validating user input.<br>
 * It includes methods for validating names, email addresses, and dates of birth.<br>
 * Implementations of this interface should ensure that:<br>
 * - Names are at least 2 characters long, at most 50 characters long, and can only contain letters, hyphens,
 * apostrophes, and spaces.<br>
 * - Email addresses contain an @ symbol, have a dot after the @ symbol, and are at most 254 characters long.<br>
 * - Dates of birth are in the past.<br>
 * These methods return a boolean indicating whether the input is valid or not.<br>
 */
public interface InputValidation {

    /**
     * Validates a name.
     * A name must be at least 2 characters long, at most 50 characters long,
     * and can only contain letters, hyphens, apostrophes, and spaces.
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
     */
    boolean isNameValid(String name);


    /**
     * Validates an email.
     * An email must contain an @ symbol, and have a dot after the @ symbol.
     * Max length of email is 254 characters.
     * @param email the email to validate
     * @return true if the name is valid, false otherwise
     */
    boolean isEmailValid(String email);

    /**
     * Validates a date of birth.
     * The date of birth must be in the past.
     * @param dob the date of birth to validate
     * @return true if the name is valid, false otherwise
     */
    boolean isDobValid(LocalDate dob);

}
