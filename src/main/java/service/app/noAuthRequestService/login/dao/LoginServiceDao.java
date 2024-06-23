package service.app.noAuthRequestService.login.dao;

import common.annotation.DatabaseOperation;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.security.EncryptedData;

public interface LoginServiceDao {

    record LoginServiceReturn (EncryptedData password, int uid, String email) {}

    @DatabaseOperation
    LoginServiceReturn getStoredPasswordForEmail(String email)
    throws InternalServerError, UserNotFoundException;

}
