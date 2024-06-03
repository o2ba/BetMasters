package object.security;

import common.object.security.EncryptedData;
import common.object.security.NonSensitiveData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonSensitiveDataTest {

    @Test
    void encrypt() {
        NonSensitiveData data = new NonSensitiveData("password");
        EncryptedData encryptedData = data.encrypt();
        assertNotEquals("password", encryptedData.toString());
        assertTrue(data.matchesEncryptedData(encryptedData));
    }

    @Test
    void testToString() {
        NonSensitiveData data = new NonSensitiveData("password");
        assertEquals("password", data.toString());
    }
}