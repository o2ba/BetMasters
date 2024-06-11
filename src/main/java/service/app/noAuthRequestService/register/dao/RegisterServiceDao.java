package service.app.noAuthRequestService.register.dao;

import common.annotation.DatabaseOperation;
import common.exception.InternalServerError;
import common.exception.register.DuplicateEmailException;
import common.model.security.SensitiveData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public interface RegisterServiceDao {
    @DatabaseOperation
    int addUser(String first_name,
                String last_name,
                String email,
                SensitiveData password,
                LocalDate dob,
                LocalDateTime createdAt)
    throws InternalServerError, DuplicateEmailException;
}