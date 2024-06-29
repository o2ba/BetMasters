package service.app.user.account.login;

import com.nimbusds.jose.JOSEException;
import common.exception.UnhandledErrorException;
import common.security.EncryptedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import service.app.user.account.login.exception.LoginDeniedException;
import common.security.SensitiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.user.account.login.db.LoginServiceDb;
import service.general.internal.authService.jwtTokenService.JwtTokenService;


@Service
public final class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Value("${jwt.token.lifetime}")
    private Long lifetime;

    private final LoginServiceDb loginServiceDb;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public LoginServiceImpl(LoginServiceDb loginServiceDb, JwtTokenService jwtTokenService) {
        this.loginServiceDb = loginServiceDb;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public LoginPayload login(String email, SensitiveData password)
            throws LoginDeniedException, UnhandledErrorException {
        try {
            LoginServiceDb.LoginServiceDbReturn pw = loginServiceDb.getEncryptedPassword(email);
            EncryptedData storedPassword = pw.encryptedData();
            int uid = pw.uid();

            if (password.matchesEncryptedData(storedPassword)) {
                try {
                    String jwtToken = jwtTokenService.generateEncryptedToken(email, uid, lifetime);
                    return new LoginPayload(jwtToken, uid, email);
                } catch (JOSEException e) {
                    logger.error("Error occurred while generating tokens on login", e);
                    throw new UnhandledErrorException("Error occurred while generating tokens");
                }
            } else {
                throw new LoginDeniedException("Incorrect password");
            }
        } catch (Exception e) {
            logger.error("Error occurred while verifying login credentials", e);
            throw new UnhandledErrorException("Error occurred while verifying login credentials");
        }
    }
}
