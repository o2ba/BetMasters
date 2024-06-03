package common.util.user.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.general.__deprecated.user.implementations.InputValidationManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InputValidationManagerTest {

    private InputValidationManager inputValidation;

    @BeforeEach
    void setUp() {
        inputValidation = new InputValidationManager();
    }

    @Test
    void isNameValid() {
        assertTrue(inputValidation.isNameValid("John Doe"));
        assertFalse(inputValidation.isNameValid("J"));
        assertFalse(inputValidation.isNameValid("This name is way too long to be considered a valid name because it has more than fifty characters"));
    }

    @Test
    void isEmailValid() {
        assertTrue(inputValidation.isEmailValid("test@example.com"));
        assertFalse(inputValidation.isEmailValid("invalidemail.com"));
        assertFalse(inputValidation.isEmailValid("invalid@com"));
    }

    @Test
    void isDobValid() {
        assertTrue(inputValidation.isDobValid(LocalDate.of(2000, 1, 1)));
        assertFalse(inputValidation.isDobValid(LocalDate.now().plusDays(1)));
    }
}