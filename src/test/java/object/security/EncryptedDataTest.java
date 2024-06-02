package object.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptedDataTest {

    @Test
    void testToString() {
        EncryptedData encryptedData = new EncryptedData("password");
        assertEquals("password", encryptedData.toString());
    }
}