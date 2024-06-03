package common.util.auth;

import common.exception.register.ValidationException;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * This utility class is responsible for validating various types of user input.
 * It provides methods to validate email, name, token expiry, and date of birth.
 * Each method throws a {@link ValidationException} if the validation fails.
 *
 * <p>Here is a brief description of each method:</p>
 *
 * <ul>
 *   <li>{@link #validateEmail(String)}: Validates an email address. The email must contain an @ symbol,
 *   and have a dot after the @ symbol. The maximum length of the email is 254 characters.</li>
 *
 *   <li>{@link #validateName(String)}: Validates a name. A name must be at least 2 characters long,
 *   at most 50 characters long, and can only contain letters, hyphens, apostrophes, and spaces.</li>
 *
 *   <li>{@link #validateTokenExpiry(Timestamp)}: Ensures a token has not expired based on the current time.
 *   The timestamp must be in the past.</li>
 *
 *   <li>{@link #validateDob(Date)}: Validates a date of birth. The date of birth must be in the past.</li>
 * </ul>
 *
 * <p>Each method in this class throws a {@link ValidationException} if the validation fails.
 * The exception message provides more details about the validation error.</p>
 *
 * @see ValidationException
 */
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