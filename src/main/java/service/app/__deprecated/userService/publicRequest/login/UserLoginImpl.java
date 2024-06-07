package service.app.__deprecated.userService.publicRequest.login;

import common.annotation.DatabaseOperation;
import common.object.security.EncryptedData;
import common.object.security.SensitiveData;
import service.general.__deprecated.user.JwtTokenUtil;
import dto.request.PostgresRequest;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import service.general.__deprecated.tokenService.RefreshTokenService;
import service.general.__deprecated.tokenService.TokenPayload;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class implements the UserLogin interface and provides the logic for user login operations.
 * It uses a Logger for logging, JwtTokenUtil for JWT token operations, PostgresRequest for database operations,
 * and RefreshTokenService for refresh token operations.
 */
public class UserLoginImpl implements UserLogin {

    private final Logger logger;
    private final JwtTokenUtil jwtUtil;
    private final PostgresRequest postgresRequest;
    private final RefreshTokenService refreshTokenService;

    /**
     * Constructs a new UserLoginImpl with the given logger, JwtTokenUtil, PostgresRequest, and RefreshTokenService.
     *
     * @param logger the Logger to use for logging
     * @param jwtUtil the JwtTokenUtil to use for JWT token operations
     * @param postgresRequest the PostgresRequest to use for database operations
     * @param refreshTokenService the RefreshTokenService to use for refresh token operations
     */
    public UserLoginImpl(Logger logger, JwtTokenUtil jwtUtil, PostgresRequest postgresRequest, RefreshTokenService refreshTokenService) {
        this.logger = logger;
        this.jwtUtil = jwtUtil;
        this.postgresRequest = postgresRequest;
        this.refreshTokenService = refreshTokenService;
    }


    /**
     * Retrieves the hashed password and uid from the database for a given email.
     *
     * @param email The email of the user.
     * @return The hashed password and uid or null if the email is not found.
     * @throws SQLException If the query fails.
     */
    @DatabaseOperation
    @Nullable
    @Override
    public PassWordUIDPayload getHashedPasswordForUser(String email)
    throws SQLException {
        try (ResultSet rs = postgresRequest.executeQuery(SQLQueries.LOGIN_USER.getQuery(), email)) {
            if (rs.next()) {
                return new PassWordUIDPayload(new EncryptedData(
                        rs.getString("password")),
                        rs.getInt("uid")
                );
            } else {
                return null;
            }
        }
    }

    /**
     * Checks if the password matches the hashed password in the database.
     *
     * @param password           The password to check.
     * @param passWordUIDPayload The hashed password and uid from the database.
     * @return True if the password matches, false otherwise.
     */
    @Override
    public boolean passwordMatches(SensitiveData password, PassWordUIDPayload passWordUIDPayload) {
        return password.matchesEncryptedData(passWordUIDPayload.password());
    }

    /**
     * Issues a JWT and a fresh refresh token for a user.
     *
     * @param uid The unique identifier of the user.
     * @return The JWT and refresh token (JWT is encrypted, refresh token is not).
     */
    @Override
    public TokenPayload.UserTokenPayload issueTokens(int uid)
    throws SQLException {
        // TODO after implementing the refresh token service
        return new TokenPayload.UserTokenPayload(
                "Bearer <token>",
                "<token>"
        );
    }
}