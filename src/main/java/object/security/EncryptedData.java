package object.security;

/**
 * Class for EncryptedObject. An EncryptedObject object is a representation of an object that has been encrypted.
 * For example, a password or a revolving refresh token.
 * The reason we have this class it to clearly distinguish between encrypted and non-encrypted objects.
 * Does not have a toString override intentionally to prevent logging of encrypted data.
 *
 * @param encryptedData Encrypted Token
 */
public record EncryptedData(String encryptedData) {

    /**
     * Constructor for EncryptedObject.
     *
     * @param encryptedData Encrypted Token
     */
    public EncryptedData {
        if (encryptedData == null) {
            throw new IllegalArgumentException("Secret cannot be null");
        }
    }

    @Override
    public String toString() {
        return encryptedData;
    }
}
