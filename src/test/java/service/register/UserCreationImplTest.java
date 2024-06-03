package service.register;

import common.object.security.SensitiveData;
import org.junit.jupiter.api.Test;
import service.userService.publicRequests.register.UserCreationImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

public class UserCreationImplTest {

    UserCreationImpl userCreation = new UserCreationImpl();

    @Test
    public void testIsUserInputValid_AllValidationsPass() {

        boolean isValid = userCreation.isUserInputValid(
                "John",
                "Doe",
                "john.doe@example.com",
                new SensitiveData("valid!Password123"),
                LocalDate.of(2000, 1, 1));

        assertTrue(isValid);
    }

    @Test
    public void testIsUserInputValid_EmailValidationFails() {
        boolean isValid = userCreation.isUserInputValid(
                "John",
                "Doe",
                "john.doeexample.com",
                new SensitiveData("valid!Password123"),
                LocalDate.of(2000, 1, 1));

        assertFalse(isValid);
    }

}