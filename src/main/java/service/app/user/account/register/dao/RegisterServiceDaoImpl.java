package service.app.user.account.register.dao;

import common.exception.UnhandledErrorException;
import common.exception.gen.TimeoutException;
import common.security.SensitiveData;
import service.general.external.dbRequest.DbRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.account.register.exception.DuplicateEmailException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("all")
final class RegisterServiceDaoImpl implements RegisterServiceDao {

    private final String ADD_USER = "INSERT INTO users (first_name, last_name, email, password, dob, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING uid";

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceDaoImpl.class);

    DbRequest dbRequest;

    @Autowired
    public RegisterServiceDaoImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public int addUser(String first_name, String last_name, String email, SensitiveData password, LocalDate dob, LocalDateTime createdAt)
    throws UnhandledErrorException, DuplicateEmailException, TimeoutException {
        try {
            List<Map<String, Object>> q = dbRequest.query(ADD_USER, first_name, last_name, email, password.encrypt().toString(), dob, createdAt);

            if (q != null && q.size() > 0) {
                return (int) q.get(0).get("uid");
            } else {
                logger.error("Could not add user: No uid returned");
                throw new UnhandledErrorException("Error occurred while adding user");
            }
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                throw new DuplicateEmailException("Email already exists");
            } else {
                // General SQL error handling
                logger.error("Could not add user due to database issue", e);
                throw new UnhandledErrorException("Error occurred while adding user");
            }
        } catch (NullPointerException npe) {
            // Handle NullPointerException in the rare case it occurs
            logger.error("NullPointerException occurred while adding user", npe);
            throw new UnhandledErrorException("Error occurred while adding user");
        }
    }

}