package service.app.noAuthRequestService.login;


import com.nimbusds.jose.JOSEException;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.exception.login.WrongEmailPasswordException;
import common.security.EncryptedData;
import common.security.SensitiveData;
import common.record.LoginPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.noAuthRequestService.login.dao.LoginServiceDao;
import service.app.authRequestService.authService.jwtTokenService.JwtTokenService;

/**
 * Basic login service using JWT only
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final LoginServiceDao loginServiceDao;
    private final JwtTokenService jwtTokenService;

    /**
     * Constructor
     */
    @Autowired
    public LoginServiceImpl(LoginServiceDao loginServiceDao, JwtTokenService jwtTokenService) {
        this.loginServiceDao = loginServiceDao;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Logs in a user with the given email and password.
     * Returns a token payload if successful.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return the token payload
     * @throws UserNotFoundException if the user is not found
     * @throws InternalServerError   if an internal error occurs
     */
    @Override
    public LoginPayload login(String email, SensitiveData password)
    throws UserNotFoundException, InternalServerError, WrongEmailPasswordException
    {
        LoginServiceDao.LoginServiceReturn userReturn = loginServiceDao.getStoredPasswordForEmail(email);
        EncryptedData pw = userReturn.password();
        if (password.matchesEncryptedData(pw)) {
            try {
                return new LoginPayload(jwtTokenService.generateEncryptedToken(
                        email,
                        userReturn.uid(),
                        86400000L
                ), null, userReturn.uid(), userReturn.email());
            } catch (JOSEException e) {
                throw new InternalServerError("Error generating token");
            }

        } else {
            throw new WrongEmailPasswordException();
        }
    }
}
