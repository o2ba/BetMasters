package service.app.noAuthRequestService.login.dao;

import common.annotation.DatabaseOperation;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.model.security.EncryptedData;
import dto.request.PostgresRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

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

        try (ResultSet rs = postgresRequest.executeQuery(LoginServiceQueries.GET_USER_BY_EMAIL_RETURN_ID.getQuery(), email)) {
            if (rs.next()) {

                System.out.println("Password: " + rs.getString("password"));
                System.out.println("UID: " + rs.getInt("uid"));

                return new LoginServiceReturn( new EncryptedData(rs.getString("password")),
                        rs.getInt("uid"), rs.getString("email"));
            } else {
                throw new UserNotFoundException("User not found with email: " + email + ".");
            }
        } catch (Exception e) {
            throw new InternalServerError("Error getting stored password for email: " + e.getMessage());
        }
    }
}
