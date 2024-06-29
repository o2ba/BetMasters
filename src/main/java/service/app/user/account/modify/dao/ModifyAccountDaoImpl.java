package service.app.user.account.modify.dao;

import common.exception.InternalServerError;
import common.security.SensitiveData;
import service.app.user.account.register.exception.DuplicateEmailException;
import service.general.PostgresRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class ModifyAccountDaoImpl implements ModifyAccountDao {

    PostgresRequest postgresRequest;

    @Autowired
    public ModifyAccountDaoImpl(PostgresRequest postgresRequest) {
        this.postgresRequest = postgresRequest;
    }

    @Override
    public int deleteAccount(String jwtToken, int uid, String email) throws InternalServerError {
        try {
            return postgresRequest.executeUpdate(ModifyAccountQueries.DELETE_ACCOUNT.getQuery(), uid);
        } catch (SQLException e) {
            throw new InternalServerError("Error deleting account: " + e.getMessage());
        }
    }

    @Override
    public int changePassword(String jwtToken, int uid, String email, SensitiveData newPassword) throws InternalServerError {
        try {
            return postgresRequest.executeUpdate(ModifyAccountQueries.CHANGE_PASSWORD.getQuery(), newPassword.encrypt().toString(), uid);
        } catch (SQLException e) {
            throw new InternalServerError("Error updating password: " + e.getMessage());
        }
    }

    @Override
    public int changeEmail(String jwtToken, int uid, String email, String newEmail) throws InternalServerError, DuplicateEmailException {
        try {
            return postgresRequest.executeUpdate(ModifyAccountQueries.CHANGE_EMAIL.getQuery(), newEmail, uid);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw new InternalServerError("Error updating email: " + e.getMessage());
        }
    }
}
