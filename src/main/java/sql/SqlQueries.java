package main.java.sql;

/**
 * Contains all the SQL queries used in the application.
 */
public final class SqlQueries {
    public static String ADD_USER = "INSERT INTO users (email, password) VALUES (?, ?) RETURNING uid";
    public static String ADD_EMAIL_VERIFICATION_TOKEN = "INSERT INTO email_verification (token, uid) VALUES (?, ?)";
    public static String DELETE_EMAIL_VERIFICATION_GET_UID_EXPIRY = "DELETE FROM email_verification WHERE token = ? RETURNING uid, expiry";
    public static String VERIFY_USER = "UPDATE users SET email_verified = true WHERE uid = ?";
    public static String FIND_USER = "SELECT * FROM users WHERE uid = ?";
    public static String DELETE_USER = "DELETE FROM users WHERE uid = ?";
}
