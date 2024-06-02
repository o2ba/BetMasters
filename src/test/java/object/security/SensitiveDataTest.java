package object.security;

import exception.register.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensitiveDataTest {

    @Test
    void validateData() {
        SensitiveData data = new SensitiveData("password");
        assertThrows(ValidationException.class, data::validateData);
        SensitiveData data2 = new SensitiveData("Password1!");
        assertDoesNotThrow(data2::validateData);
    }

    @Test
    void testToString() {
        SensitiveData data3 = new SensitiveData("password");
        assertThrows(UnsupportedOperationException.class, data3::toString);
    }
}