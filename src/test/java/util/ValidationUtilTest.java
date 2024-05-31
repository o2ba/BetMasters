package util;

import exception.user.ValidationException;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    ValidationUtil validationUtil = new ValidationUtil();

    @Test
    void validateEmail() {
        String tooLong = String.join("",
                Collections.nCopies(64, "a")) + "@" +
                String.join("",
                Collections.nCopies(190, "b")) + ".com";

        assertThrows(ValidationException.class,
                () -> validationUtil.validateEmail("invalid email"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validateEmail("invalid@email"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validateEmail(tooLong));

        assertDoesNotThrow(() -> validationUtil.validateEmail("valid@email.com"));
    }

    @Test
    void validateName() {
        String tooLong = String.join("",
                Collections.nCopies(51, "a"));

        assertThrows(ValidationException.class,
                () -> validationUtil.validateName("a"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validateName(tooLong));
        assertThrows(ValidationException.class,
                () -> validationUtil.validateName("invalid name!"));

        assertDoesNotThrow(() -> validationUtil.validateName("valid-name"));
    }

    @Test
    void validatePassword() {
        String tooLong = String.join("",
                Collections.nCopies(15, "a!A2"));

        assertThrows(ValidationException.class,
                () -> validationUtil.validatePassword("a"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validatePassword(tooLong));
        assertThrows(ValidationException.class,
                () -> validationUtil.validatePassword("invalidpassword"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validatePassword("invalidPASSWORD"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validatePassword("invalid123456"));
        assertThrows(ValidationException.class,
                () -> validationUtil.validatePassword("invalidPassword"));

        assertDoesNotThrow(() -> validationUtil.validatePassword("jasSowa129ed!"));
        assertDoesNotThrow(() -> validationUtil.validatePassword("valid!Password1"));
    }

    @Test
    void validateTokenExpiry() {
        Timestamp expired = new Timestamp(System.currentTimeMillis() - 1000);
        Timestamp future = new Timestamp(System.currentTimeMillis() + 1000);

        assertDoesNotThrow(() -> validationUtil.validateTokenExpiry(expired));
        assertThrows(ValidationException.class,
                () -> validationUtil.validateTokenExpiry(future));
    }

    @Test
    void validateDob() {

    }
}