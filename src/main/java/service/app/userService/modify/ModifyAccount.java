package service.app.userService.modify;

import com.google.gson.JsonElement;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.security.SensitiveData;


public interface ModifyAccount {

    /**
     * Deletes the account of the user with the given uid and email.
     * @param jwtToken the token of the user
     * @param uid the id of the user
     * @param email the email of the user
     * @throws InternalServerError if an internal server error occurs
     * @throws NotAuthorizedException if the user is not authorized
     */
    void deleteAccount(String jwtToken, int uid, String email)
    throws InternalServerError, NotAuthorizedException;

    void changePassword(String jwtToken, int uid, String email, SensitiveData newPassword)
    throws InternalServerError, NotAuthorizedException, ValidationException;

    JsonElement changeEmail(String jwtToken, int uid, String email, String newEmail)
    throws InternalServerError, NotAuthorizedException, DuplicateEmailException, ValidationException;
}
