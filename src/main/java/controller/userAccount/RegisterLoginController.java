package controller.userAccount;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.exception.UnhandledErrorException;
import service.app.userService.login.exception.LoginDeniedException;
import common.security.SensitiveData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.userService.login.LoginService;
import service.app.userService.register.RegisterService;
import service.app.userService.register.exception.DuplicateEmailException;
import service.app.userService.register.exception.InvalidAgeException;
import service.app.userService.register.exception.InvalidInputException;

import java.time.LocalDate;

@RestController
@Api(tags = "Register and Login")
public class RegisterLoginController {

    private final LoginService loginService;
    private final RegisterService registerService;

    private static final Logger logger = LoggerFactory.getLogger(RegisterLoginController.class);

    @Autowired
    public RegisterLoginController(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;

    }

    @ApiOperation(value = "Login a user", response = String.class, tags = "Register and Login")
    @PostMapping("/user/login")
    public ResponseEntity<String> login(
            @ApiParam(value = "The email of the user.", required = true) @RequestParam String email,
            @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password) {
        try {
            LoginService.LoginPayload tp = loginService.login(email, password);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            String prettyJson = gsonBuilder.create().toJson(tp);
            return ResponseEntity.ok(prettyJson);
        } catch (LoginDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login denied");
        } catch (UnhandledErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @ApiOperation(value = "Register a new user", response = String.class, tags = "Register and Login")
    @PostMapping("/user/register")
    public ResponseEntity<String> register(
            @ApiParam(value = "The first name of the user.", required = true) @RequestParam String first_name,
            @ApiParam(value = "The last name of the user.", required = true) @RequestParam String last_name,
            @ApiParam(value = "The email of the user.", required = true) @RequestParam String email,
            @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password,
            @ApiParam(value = "The date of birth of the user (YYYY-MM-DD).", required = true) @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob) {
        try {
            JsonObject tp = registerService.addUser(first_name, last_name, email, password, dob);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            String prettyJson = gsonBuilder.create().toJson(tp);
            return ResponseEntity.ok(prettyJson);
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        } catch (InvalidInputException e) {
            return switch (e.getType()) {
                case EMAIL -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
                case NAME -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid name");
                case PASSWORD -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
                case DOB -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date of birth");
            };
        } catch (InvalidAgeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid age");
        } catch (UnhandledErrorException e) {
            logger.error("Error occurred while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
