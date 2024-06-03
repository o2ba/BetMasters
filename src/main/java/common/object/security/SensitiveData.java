package common.object.security;

import common.exception.register.ValidationException;

public final class SensitiveData extends NonSensitiveData {

    /**
     * Constructor for SensitiveData.
     *
     * @param rawData the raw data
     */
    public SensitiveData(String rawData) {
        super(rawData);
    }

    /**
     * Validates the data. The password must be at least 8 characters long, contain at least one lowercase letter,
     * one uppercase letter, one symbol and one digit.
     * @throws ValidationException if the data is invalid
     */
    public void validateData() throws ValidationException {
        if (!rawData.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,50}$")) {
            throw new ValidationException("Password must be at least 8 characters long, " +
                    "contain at least one lowercase letter, one uppercase letter, one symbol and one digit");
        }
    }

    /**
     * Disables the toString method for SensitiveData objects.
     * @return Nothing. Throws an UnsupportedOperationException
     * @throws UnsupportedOperationException if the method is called
     */
    @Override
    public String toString() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Sensitive data cannot be converted to string");
    }
}
