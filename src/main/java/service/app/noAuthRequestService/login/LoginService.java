package service.app.noAuthRequestService.login;

import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.exception.login.WrongEmailPasswordException;
import common.model.security.SensitiveData;
import common.record.TokenPayload;

public interface LoginService {
    TokenPayload login(String username, SensitiveData password)
    throws UserNotFoundException, InternalServerError, WrongEmailPasswordException;
}
