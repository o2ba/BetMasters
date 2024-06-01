package service;


/**
 * <p>Creates and manages refresh tokens.
 * A refresh token is valid for 30 days. It is encrypted with Bcrypt.
 * </p>
 *
 * <b>Security Features</b>
 * <ul>
 *     <li>Short-lived validity for individual refresh token (15 days)</li>
 *     <li>Rotating refresh tokens</li>
 *     <li>Bound to an IP address</li>
 *     <li>Encrypted in backend</li>
 *     <li>Stored in a secure HttpOnly cookie</li>
 * </ul>
 *
 * <b>Usability Features</b>
 * <ul>
 *     <li>Once a user is authenticated, they will not need to provide credentials until logged out.</li>
 *     <li>A new IP address will not destroy the refresh token but will require re-authentication.</li>
 *     <li>Users can have multiple refresh tokens with different IP addresses.</li>
 * </ul>
 */
public class RefreshTokenService {

}
