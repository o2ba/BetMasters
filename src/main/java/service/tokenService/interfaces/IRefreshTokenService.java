package service.tokenService.interfaces;


import common.exception.login.WrongEmailPasswordException;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Interface handling the refresh token service.
 */
public interface IRefreshTokenService {

    /**
     * A record of the refresh token data.
     * This includes all the data needed to handle the refresh token.
     * @param refreshToken The refresh token.
     */
    record refreshTokenData(String refreshToken, int uid, LocalDateTime expDate) {}

    /**
     * Retrieves the refresh token for a given user ID.<br>
     * <b>Logic:</b>
     * <ol>
     *     <li>Queries the database for the row with the given user ID.
     *     This query is sorted by the most recent refresh token in descending order.</li>
     *     <li>Checks amount of rows returned.</li>
     *     <ul>
     *         <li>If = 0, throws WrongEmailPasswordException.</li>
     *         <li>If => 1, returns the first row.</li>
     *     </ul>
     *     <li>Returns the refresh token.</li>
     * </ol>
     *
     * @param uid The unique identifier of the user.
     * @return The refresh token.
     * @throws SQLException If the query fails. This exception is logged.
     * @throws WrongEmailPasswordException If the user ID is not found.
     * @see IRefreshTokenService#refreshTokenMatches(refreshTokenData, String)  to actually check if the refresh token matches.
     */
    refreshTokenData retrieveRefreshToken(int uid)
    throws SQLException, WrongEmailPasswordException;

    /**
     * Checks if the refresh token matches the one in the database.<br>
     * Simple comparison of the refresh token given and the one in the database.<br>
     * To retrieve the refresh token, use {@link IRefreshTokenService#retrieveRefreshToken(int)}.<br>
     * @return True if the refresh token matches, false otherwise.
     */
    boolean refreshTokenMatches(refreshTokenData refreshToken, String userRefreshToken);

    /**
     * Calculates the age of a refresh token.<br>
     * @param refreshToken The refresh token to calculate the age of.
     * @return The age of the refresh token in days
     */
    int calculateTokenAgeInDays(refreshTokenData refreshToken);

    /**
     * Issues a new refresh token for a given user ID and deletes the old one.<br>
     *
     * <b>Logic:</b>
     * <ol>
     *     <li>Generates a new refresh token.</li>
     *     <li>Delete the old refresh token from the database.</li>
     *     <li>Insert the new refresh token into the database.</li>
     *     <li>Returns the new refresh token.</li>
     * </ol>
     *
     * @param refreshToken The refresh token data.
     * @return The new refresh token.
     */
    String issueNewRefreshToken(refreshTokenData refreshToken)
    throws SQLException;

}
