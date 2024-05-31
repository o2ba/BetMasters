package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionUtil {

    private static final int STRENGTH = 10;

    private final BCryptPasswordEncoder passwordEncoder;

    public EncryptionUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder(STRENGTH);
    }

    public String encrypt(String encodeString) {
        return passwordEncoder.encode(encodeString);
    }

    public boolean matches(String rawPassword, String encodedString) {
        return passwordEncoder.matches(rawPassword, encodedString);
    }
}