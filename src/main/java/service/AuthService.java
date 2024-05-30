package main.java.service;

import main.java.builder.ApiRequest;
import main.java.builder.PostgresRequest;
import main.java.exception.sql.DuplicateEmailException;
import main.java.exception.sql.RateLimitException;
import main.java.exception.sql.WrongPasswordRateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * AuthService allows the creation, modification and deletion of users.
 */
@Deprecated
public class AuthService {

    /** Logger for the ApiRequest class */
    private static final Logger logger = LoggerFactory.getLogger(ApiRequest.class);

    private static final String ADDUSERTODB = "INSERT INTO users " +
            "(email, password) VALUES (?, ?) RETURNING uid";

    private static final String ADDEMAILVERIFICATIONTOKEN
            = "INSERT INTO email_verification (token, uid) VALUES (?, ?)";

    private final String FINDUSER = "SELECT * FROM users WHERE email = ?";

    private final String DELETEUSER = "DELETE FROM users WHERE uid = ?";

    /**
     * Constructor for UserRequest.
     */
    public AuthService() { }

    /**
     * Creates a new user. The password is hashed before being stored in the database.
     * @param email the username of the user
     * @param password the password of the user
     * @param dob the date of birth of the user
     * @throws DuplicateEmailException if the email already exists in the database
     * @throws RateLimitException if the user has made too many requests
     * @throws SQLException if there is an error with the SQL query
     */
    public void createUser(String firstName, String lastName, String email, String password, String dob)
            throws DuplicateEmailException, RateLimitException, SQLException {

        var dbRequest = new PostgresRequest();
        int uid;

        try {
            var passwordEncoder = new BCryptPasswordEncoder();
            // TODO FIX VULNERABILITY: CVE-2022-22976
            String encodedPassword = passwordEncoder.encode(password);
            ResultSet resultSet = dbRequest.executeQuery(ADDUSERTODB, email, encodedPassword);

            if (resultSet.next()) {
                uid = resultSet.getInt("uid");
                logger.debug("User created with uid: " + uid);
                createVerificationToken(uid);
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            if (e.getSQLState().equals("23505")) {
                logger.debug("SQLException: A user with this email already exists.");
                throw new DuplicateEmailException();
            } else {
                throw new SQLException();
            }
        }
    }

    /**
     * Inserts to the "email_verification" table the user id (FK) with a verification token
     * Used to verify the account
     * @param uid the user id
     * @throws SQLException if there is an error with the SQL query
     */
    private void createVerificationToken(int uid)
            throws SQLException {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder(15);

        for (int i = 0; i < 15; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        String emailVerificationToken = builder.toString();
        var dbRequest = new PostgresRequest();
        dbRequest.executeUpdate(ADDEMAILVERIFICATIONTOKEN, emailVerificationToken, uid);
    }

    /**
     *
     */
    private void login(String username, String password)
            throws SQLException, RateLimitException, WrongPasswordRateLimitException {

    }

    public static void main(String[] args) throws Exception {
        var authService = new AuthService();
        authService.createUser("test", "test", "tesst", "test", "test");
    }



}
