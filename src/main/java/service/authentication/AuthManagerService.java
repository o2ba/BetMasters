package service.authentication;

import common.util.auth.JwtTokenUtil;

public class AuthManagerService {

    /**
     * Defines the status of a JWT.
     * VALID: The JWT is valid.
     * INVALID_SIGNATURE: The JWT is invalid. This could be due to a malformed JWT or an invalid signature.
     * INVALID_USER_MISMATCH: The JWT is invalid because the user in the JWT does not match the user in the request.
     * EXPIRED: The JWT has expired.
     * If the JWT is valid, the user is authenticated, and the request is allowed to proceed.
     * If the JWT is invalid, the user is not authenticated, and the request is denied. The
     * user is redirected to the login page.
     * If the JWT has expired, the user is not authenticated, and the request is denied. The
     */
    private enum jwtStatus {
        VALID,
        INVALID_SIGNATURE,
        INVALID_USER_MISMATCH,
        EXPIRED
    }

    /** TokenUtil object to handle JWTs. */
    private JwtTokenUtil tokenUtil;


    /**
     * Gets the status of a JWT for a given user.
     * @param jwt The JWT to check.
     * @return The status of the JWT.
     * @see jwtStatus
     */
    private jwtStatus getJwtStatus(String jwt, String email) {

        if(jwt == null || jwt.isEmpty()) {
            return jwtStatus.INVALID_SIGNATURE;
        }

        // tokenUtil.verifyToken(jwt):

        return jwtStatus.VALID;
    }


}
