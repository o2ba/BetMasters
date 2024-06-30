package controller.user.transaction;

import common.exception.UnhandledErrorException;
import controller.ResponseFormer;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.app.user.activity.transact.exception.InvalidTransactionException;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;

@RestControllerAdvice
@Order(1)
public class TransactionAdvice {
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<String> handleInvalidTransactionException(InvalidTransactionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<String> handleNotEnoughBalanceException(NotEnoughBalanceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UnhandledErrorException.class)
    public ResponseEntity<String> handleUnhandledErrorException(UnhandledErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }
}
