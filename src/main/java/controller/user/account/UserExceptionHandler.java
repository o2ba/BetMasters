package controller.user.account;

import common.exception.UnhandledErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.app.user.account.login.exception.LoginDeniedException;
import service.app.user.account.register.exception.DuplicateEmailException;
import service.app.user.account.register.exception.InvalidAgeException;
import service.app.user.account.register.exception.InvalidInputException;


@RestControllerAdvice
@Ord
public class UserExceptionHandler {
    @ExceptionHandler(value = LoginDeniedException.class)
    public ResponseEntity<String> handleLoginDeniedException(LoginDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login denied");
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException e) {
        return switch (e.getType()) {
            case EMAIL -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
            case NAME -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid name");
            case PASSWORD -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
            case DOB -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date of birth");
        };
    }

    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<String> handleInvalidAgeException(InvalidAgeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid age");
    }

    @ExceptionHandler(UnhandledErrorException.class)
    public ResponseEntity<String> handleUnhandledErrorException(UnhandledErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
