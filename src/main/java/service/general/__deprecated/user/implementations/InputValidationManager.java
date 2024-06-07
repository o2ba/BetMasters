package service.general.__deprecated.user.implementations;

import service.general.__deprecated.user.interfaces.InputValidation;

import java.time.LocalDate;

public class InputValidationManager implements InputValidation {

    /**
     * Validates a name.
     * A name must be at least 2 characters long, at most 50 characters long,
     * and can only contain letters, hyphens, apostrophes, and spaces.
     *
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
     */
    @Override
    public boolean isNameValid(String name) {
        if(name.length() < 2) {
            return false;
        } else if(name.length() > 50) {
            return false;
        } else return name.matches("^[a-zA-Z-'\\s]+$");
    }

    /**
     * Validates an email.
     * An email must contain an @ symbol, and have a dot after the @ symbol.
     * Max length of email is 254 characters.
     *
     * @param email the email to validate
     * @return true if the name is valid, false otherwise
     */
    @Override
    public boolean isEmailValid(String email) {
        if (!email.matches("^.+@.+\\..+$")) {
            return false;
        } else return !(email.length() > 254);
    }

    /**
     * Validates a date of birth.
     * The date of birth must be in the past.
     *
     * @param dob the date of birth to validate
     * @return true if the name is valid, false otherwise
     */
    @Override
    public boolean isDobValid(LocalDate dob) {
        LocalDate now = LocalDate.now();
        return dob.isBefore(now);
    }
}
