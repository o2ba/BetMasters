package controller.user.bet;

import common.exception.UnhandledErrorException;
import controller.ResponseFormer;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.user.activity.bet.exception.*;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;

@RestControllerAdvice
@Order(1)
public class BetControllerAdvice {
    @ExceptionHandler(UnhandledErrorException.class)
    public ResponseEntity<String> handleUnhandledErrorException(UnhandledErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BettingNotOpenException.class)
    public ResponseEntity<String> handleBettingNotOpenException(BettingNotOpenException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoOddsForGameException.class)
    public ResponseEntity<String> handleNoOddsForGameException(NoOddsForGameException e) {
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

    @ExceptionHandler(FixtureNotFoundException.class)
    public ResponseEntity<String> handleFixtureNotFoundException(FixtureNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(StatusAlreadyIdentical.class)
    public ResponseEntity<String> handleStatusAlreadyIdentical(StatusAlreadyIdentical e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFormer.formErrorResponse(e.getMessage()));
    }
}