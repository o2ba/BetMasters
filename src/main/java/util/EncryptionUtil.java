package main.java.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionUtil {

    private static final int STRENGTH = 10;

    private final BCryptPasswordEncoder passwordEncoder;

    public EncryptionUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder(STRENGTH);
    }

    public String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}