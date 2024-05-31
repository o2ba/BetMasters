package sql;

/**
 * Contains all the SQL queries used in the application.
 */
public final class SqlQueries {
    public static String ADD_USER = "INSERT INTO users (first_name, last_name, email, password, dob) VALUES (?, ?, ?, ?, ?) RETURNING uid";
    public static String ADD_EMAIL_VERIFICATION_TOKEN = "INSERT INTO email_verification (token, uid, expiry) VALUES (?, ?, ?)";
    public static String CHECK_EMAIL_EXISTS = "SELECT * FROM users WHERE email = ?";
    public static String DELETE_EMAIL_VERIFICATION_GET_UID_EXPIRY = "DELETE FROM email_verification WHERE token = ? RETURNING uid, expiry";
    public static String VERIFY_USER = "UPDATE users SET email_verified = true WHERE uid = ?";
    public static String DELETE_USER = "DELETE FROM users WHERE uid = ?";

    public static String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static String GET_USER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";

    public static String STORE_REFRESH_TOKEN = "INSERT INTO refresh_toksens (token, uid, expiry) VALUES (?, ?, ?)";
    public static String GET_USER_BY_REFRESH_TOKEN = "SELECT * FROM users INNER JOIN refresh_tokens ON users.uid = refresh_tokens.uid WHERE refresh_tokens.token = ?";
    public static String INVALIDATE_REFRESH_TOKEN = "DELETE FROM refresh_tokens WHERE token = ?";
}
