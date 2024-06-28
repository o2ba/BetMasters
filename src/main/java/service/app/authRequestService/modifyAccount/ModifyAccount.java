package service.app.authRequestService.modifyAccount;

import com.google.gson.JsonElement;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.security.SensitiveData;


public interface ModifyAccount {
    void deleteAccount(String jwtToken, int uid, String email)
    throws InternalServerError, NotAuthorizedException;

    void changePassword(String jwtToken, int uid, String email, SensitiveData newPassword)
    throws InternalServerError, NotAuthorizedException, ValidationException;

    JsonElement changeEmail(String jwtToken, int uid, String email, String newEmail)
    throws InternalServerError, NotAuthorizedException, DuplicateEmailException, ValidationException;
}
