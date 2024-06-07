package service.app.__deprecated.userService.publicRequest.login;


import common.annotation.DatabaseOperation;
import common.object.security.EncryptedData;
import common.object.security.SensitiveData;
import org.jetbrains.annotations.Nullable;
import service.general.__deprecated.tokenService.TokenPayload;

import java.sql.SQLException;

/** Handles the login of a user */
public interface UserLogin {


    /** Record the password and uid. */
    record PassWordUIDPayload (EncryptedData password, int uid) { }

    /** SQL Queries */
    enum SQLQueries {
        LOGIN_USER("SELECT * FROM users WHERE username = ?");

        private final String query;

        SQLQueries(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }

    /**
     * Retrieves the hashed password and uid from the database for a given email.
     * @param email The email of the user.
     * @return The hashed password and uid or null if the email is not found.
     * @throws SQLException If the query fails.
     */
    @DatabaseOperation
    @Nullable
    PassWordUIDPayload getHashedPasswordForUser(String email)
    throws SQLException;

    /**
     * Checks if the password matches the hashed password in the database.
     * @param password The password to check.
     * @param passWordUIDPayload The hashed password and uid from the database.
     * @return True if the password matches, false otherwise.
     */
    boolean passwordMatches(SensitiveData password, PassWordUIDPayload passWordUIDPayload);

    /**
     * Issues a JWT and a fresh refresh token for a user.
     * @param uid The unique identifier of the user.
     * @return The JWT and refresh token (JWT is encrypted, refresh token is not).
     */
    @DatabaseOperation
    TokenPayload.UserTokenPayload issueTokens(int uid)
    throws SQLException;

}
