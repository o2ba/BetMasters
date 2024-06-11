package controller;

import com.google.gson.Gson;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.exception.login.WrongEmailPasswordException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.model.security.SensitiveData;
import common.record.TokenPayload;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.app.noAuthRequestService.login.LoginService;
import service.app.noAuthRequestService.register.RegisterService;

import java.time.LocalDate;

@Controller
public class NoAuthRequestController {

    private final LoginService loginService;
    private final RegisterService registerService;

    private static final Logger logger = LoggerFactory.getLogger(NoAuthRequestController.class);

    @Autowired
    public NoAuthRequestController(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;

    }

    /**
     * Controller for login. Returns a JWT token if the login is successful.
     * If the login is unsuccessful, it returns an error message.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The JWT token if the login is successful, or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
        @ApiParam(value = "The email of the user.", required = true) @RequestParam String email,
        @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password) {
        try {
            TokenPayload tp = loginService.login(email, password);
            Gson gson = new Gson();
            return ResponseEntity.ok(gson.toJson(tp));
        } catch (UserNotFoundException e) {
            // Redirect to register
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password");
        } catch (WrongEmailPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password");
        } catch (InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @ApiOperation(value = "Register a new user", response = String.class)
    @PostMapping("/register")
    public ResponseEntity<String> register(
        @ApiParam(value = "The first name of the user.", required = true) @RequestParam String first_name,
        @ApiParam(value = "The last name of the user.", required = true) @RequestParam String last_name,
        @ApiParam(value = "The email of the user.", required = true) @RequestParam String email,
        @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password,
        @ApiParam(value = "The date of birth of the user (YYYY-MM-DD).", required = true) @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob) {
        try {
            registerService.addUser(first_name, last_name, email, password, dob);
            return ResponseEntity.ok("User added successfully");
        } catch (InternalServerError e) {
            logger.error("Internal server error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input data is not valid");
        }
    }
}
