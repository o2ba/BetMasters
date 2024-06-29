package service.app.userService.modify.dao;

import common.exception.InternalServerError;
import common.exception.register.DuplicateEmailException;
import common.security.SensitiveData;

public interface ModifyAccountDao {
    int deleteAccount(String jwtToken, int uid, String email) throws InternalServerError;
    int changePassword(String jwtToken, int uid, String email, SensitiveData newPassword) throws InternalServerError;
    int changeEmail(String jwtToken, int uid, String email, String newEmail) throws InternalServerError, DuplicateEmailException;
}
