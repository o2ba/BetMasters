package util;

import exception.user.ValidationException;

import java.sql.Date;
import java.sql.Timestamp;

public class ValidationUtil {
    /**
     * Validates an email.
     * An email must contain an @ symbol, and have a dot after the @ symbol.
     * Max length of email is 254 characters.
     * @param email the email to validate
     * @throws ValidationException if the email is invalid
     */
    public void validateEmail(String email) throws ValidationException {
        if (!email.matches("^.+@.+\\..+$")) {
            throw new ValidationException("Invalid email format");
        } else if (email.length() > 254) {
            throw new ValidationException("Email must be at most 320 characters long");
        }
    }

    /**
     * Validates a name. A name must be at least 2 characters long, at most 50 characters long,
     * and can only contain letters, hyphens, apostrophes, and spaces.
     * @param name the name to validate
     * @throws ValidationException if the name is invalid
     */
    public void validateName(String name) throws ValidationException {
        if(name.length() < 2) {
            throw new ValidationException("Name must be at least 2 characters long");
        } else if(name.length() > 50) {
            throw new ValidationException("Name must be at most 50 characters long");
        } else if(!name.matches("^[a-zA-Z-'\\s]+$")) {
            throw new ValidationException("Name can only contain letters, hyphens, apostrophes, and spaces");
        }
    }

    /**
     * Validates a password. A password must be at least 8 characters long, at most 50 characters long,
     * and contain at least one lowercase letter, one uppercase letter, one symbol and one digit.
     * @param password the password to validate
     * @throws ValidationException if the password is invalid
     */
    public void validatePassword(String password) throws ValidationException {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,50}$")) {
            throw new ValidationException("Password must be at least 8 characters long, " +
                    "contain at least one lowercase letter, one uppercase letter, one symbol and one digit");
        }
    }

    /**
     * Ensures a token has not expired based on the current time.
     * @param timestamp the timestamp to validate
     * @throws ValidationException if the timestamp is in the future
     */
    public void validateTokenExpiry(Timestamp timestamp) throws ValidationException {
        if (timestamp.after(new Timestamp(System.currentTimeMillis()))) {
            throw new ValidationException("Timestamp must be in the past");
        }
    }

    /**
     * Validates a date of birth. A date of birth must be in the past.
     * @param dob the date of birth to validate
     * @throws ValidationException if the date of birth is in the future
     */
    public void validateDob(Date dob) throws ValidationException {
        if (dob.after(new Date(System.currentTimeMillis()))) {
            throw new ValidationException("Date of birth must be in the past");
        }
    }

}