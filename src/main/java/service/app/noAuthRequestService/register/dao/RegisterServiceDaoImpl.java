package service.app.noAuthRequestService.register.dao;

import common.exception.InternalServerError;
import common.exception.register.DuplicateEmailException;
import common.model.security.SensitiveData;
import dto.request.PostgresRequest;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class RegisterServiceDaoImpl implements RegisterServiceDao {

    PostgresRequest postgresRequest;

    public RegisterServiceDaoImpl(PostgresRequest postgresRequest) {
        this.postgresRequest = postgresRequest;
    }

    /**
     * Adds a user to the database
     * @param first_name The first name of the user
     * @param last_name The last name of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param dob The date of birth of the user
     * @return The id of the user
     * @throws DuplicateEmailException If the email already exists
     * @throws InternalServerError If an error occurs while adding the user
     */
    @Override
    public int addUser(String first_name, String last_name, String email, SensitiveData password, LocalDate dob, LocalDateTime createdAt)
    throws InternalServerError, DuplicateEmailException {
        try (ResultSet rs = postgresRequest.executeQuery(RegisterServiceQueries.ADD_USER.getQuery(),
                first_name, last_name, email, password.encrypt().toString(), dob, createdAt)) {
            if (rs.next()) {
                return rs.getInt("uid");
            } else {
                throw new InternalServerError("Error while adding user");
            }
        } catch (SQLException e) {
            // If the exception is from duplicate email
            if (e.getSQLState().equals("23505")) {
                throw new DuplicateEmailException("Email already exists");
            } else {
                throw new InternalServerError("Error while adding user");
            }
        }
    }

}