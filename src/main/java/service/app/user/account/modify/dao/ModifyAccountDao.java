package service.app.user.account.modify.dao;

import common.exception.InternalServerError;
import common.security.SensitiveData;
import service.app.user.account.register.exception.DuplicateEmailException;

public interface ModifyAccountDao {
    int deleteAccount(String jwtToken, int uid, String email) throws InternalServerError;
    int changePassword(String jwtToken, int uid, String email, SensitiveData newPassword) throws InternalServerError;
    int changeEmail(String jwtToken, int uid, String email, String newEmail) throws InternalServerError, DuplicateEmailException;
}
