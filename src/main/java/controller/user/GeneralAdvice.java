package controller.user;

import common.exception.NotAuthorizedException;
import controller.ResponseFormer;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(0)
public class GeneralAdvice {

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<String> handleNotAuthorizedException(NotAuthorizedException e) {
        return ResponseEntity.status(401).body(ResponseFormer.formErrorResponse("Not authorized"));
    }

}
