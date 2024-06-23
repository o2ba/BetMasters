package service.app.noAuthRequestService.login.dao;

import common.annotation.DatabaseOperation;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.security.EncryptedData;
import dto.PostgresRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class LoginServiceDaoImpl implements LoginServiceDao {

    PostgresRequest postgresRequest;

    @Autowired
    public LoginServiceDaoImpl(PostgresRequest postgresRequest) {
        this.postgresRequest = postgresRequest;
    }

    @Override
    @DatabaseOperation
    public LoginServiceReturn getStoredPasswordForEmail(String email) throws InternalServerError {
        try {
            List<Map<String, Object>> result = postgresRequest.safeExecuteQuery(LoginServiceQueries.GET_USER_BY_EMAIL_RETURN_ID.getQuery(), email);
            return new LoginServiceReturn(new EncryptedData((String) result.get(0).get("password")),
                    (int) result.get(0).get("uid"), (String) result.get(0).get("email"));
        } catch (SQLException e) {
            throw new InternalServerError("Error getting stored password for email: " + e.getMessage());
        }

    }
}
