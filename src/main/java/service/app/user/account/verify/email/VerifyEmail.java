package service.app.user.account.verify.email;

import common.exception.UnhandledErrorException;
import service.app.user.account.verify.email.exception.EmailNotFoundException;

public interface VerifyEmail {
    /**
     * Initiates the process of verifying a user's email address by sending a verification email
     * to the specified email address. The verification link will remain valid for the specified
     * lifetime duration.
     *
     * @param email    the email address that needs to be verified. Must be a valid email address format.
     * @param lifetime the time period, in minutes, for which the verification link will remain valid.
     *                 Must be a positive integer.
     * @throws EmailNotFoundException if the specified email address is not found in the system.
     * @throws UnhandledErrorException if an unexpected error occurs during the email verification process.
     */
    void sendVerificationEmail (String email, int lifetime)
            throws EmailNotFoundException, UnhandledErrorException;

}
