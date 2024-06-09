package service.app.noAuthRequestService.login.dao;

import common.annotation.DatabaseOperation;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.object.security.EncryptedData;

public interface LoginServiceDao {

    record LoginServiceReturn (EncryptedData password, int uid) {}

    @DatabaseOperation
    LoginServiceReturn getStoredPasswordForEmail(String email)
    throws InternalServerError, UserNotFoundException;

}
