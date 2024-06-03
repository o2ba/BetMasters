package service.general.__deprecated.tokenService;

import common.object.security.EncryptedData;

/**
 * This class represents the payload of the token.
 * It is used to store the information that is encoded in the token.
 */

@Deprecated
public class TokenPayload {

    /** The user stores the jwt token and the refresh token.
     * The refresh token is not encrypted on the user's side.
     */
    public record UserTokenPayload(String jwtToken, String refreshToken) { }

    /** The server stores the refresh token in an encrypted form. */
    public record ServerTokenPayload(EncryptedData refreshToken) { }

}

