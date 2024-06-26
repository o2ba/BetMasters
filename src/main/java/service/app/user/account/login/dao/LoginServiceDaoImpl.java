package service.app.user.account.login.dao;

import common.exception.UnhandledErrorException;
import common.security.EncryptedData;
import service.general.external.dbRequest.DbRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.account.login.exception.LoginDeniedException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
final class LoginServiceDaoImpl implements LoginServiceDao {

    private final String RETRIEVE_HASHED_PASSWORD = "SELECT password, uid FROM users WHERE email = ?";

    Logger logger = LoggerFactory.getLogger(LoginServiceDaoImpl.class);

    DbRequest dbRequest;

    @Autowired
    public LoginServiceDaoImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public LoginServiceDbReturn getEncryptedPassword(String email)
            throws UnhandledErrorException, LoginDeniedException {
        try {
            List<Map<String, Object>> q = dbRequest.query(RETRIEVE_HASHED_PASSWORD, email);
            if (q.isEmpty()) {
                throw new LoginDeniedException("Email not found");
            } else {
                try {
                    return new LoginServiceDbReturn(new EncryptedData((String) q.get(0).get("password")),
                            (int) q.get(0).get("uid"));
                } catch (Exception e) {
                    logger.error("Error while converting password to EncryptedData", e);
                    throw new UnhandledErrorException("Error while converting password to EncryptedData");
                }
            }
        } catch (SQLException e) {
            throw new UnhandledErrorException("Error occurred while verifying login credentials");
        }
    }
}