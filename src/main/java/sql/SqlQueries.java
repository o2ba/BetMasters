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

    public static String ADD_REFRESH_TOKEN = "INSERT INTO refresh_tokens (token, uid, issue_date, expiry_date) VALUES (?, ?, ?, ?)";
    public static String GET_REFRESH_TOKEN = "SELECT token FROM refresh_tokens WHERE uid = ?";
    public static String DELETE_ALL_REFRESH_TOKENS_FOR_USER = "DELETE FROM refresh_tokens WHERE uid = ?";
    public static String GET_UID_FROM_REFRESH_TOKEN = "SELECT uid FROM refresh_tokens WHERE token = ?";

}
