package object.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Abstract class for non-sensitive data. NonSensitiveData objects are used to store data that is not sensitive.
 */
public class NonSensitiveData {

    protected final String rawData;
    private static final int DEFAULT_STRENGTH = 10;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(DEFAULT_STRENGTH);

    /**
     * Constructor for AbstractData.
     *
     * @param rawData the raw data
     */
    public NonSensitiveData(String rawData) {
        if (rawData == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        this.rawData = rawData;
    }

    /**
     * Encrypts the data using the default encryption strength.
     *
     * @return the encrypted data
     */
    @NotNull
    public EncryptedData encrypt() {
        return new EncryptedData(passwordEncoder.encode(rawData));
    }

    /**
     * Encrypts the data using a custom encryption strength.
     *
     * @param strength the encryption strength
     * @return the encrypted data
     */
    @NotNull
    public EncryptedData encrypt(int strength) {
        BCryptPasswordEncoder customEncoder = new BCryptPasswordEncoder(strength);
        return new EncryptedData(customEncoder.encode(rawData));
    }

    /**
     * Checks if the raw data matches an encrypted string.
     *
     * @param encryptedString the encrypted string
     * @return true if the raw data matches the encrypted string, false otherwise
     */
    public boolean matchesEncryptedString(EncryptedData encryptedString) {
        return passwordEncoder.matches(rawData, encryptedString.encryptedData());
    }

    /**
     * Returns the raw data.
     *
     * @return the raw data
     */
    @Override
    public String toString() {
        return rawData;
    }
}
