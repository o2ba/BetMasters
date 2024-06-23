package service.app.noAuthRequestService.login;

import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.exception.login.WrongEmailPasswordException;
import common.model.security.SensitiveData;
import common.record.LoginPayload;

public interface LoginService {
    LoginPayload login(String username, SensitiveData password)
    throws UserNotFoundException, InternalServerError, WrongEmailPasswordException;
}
