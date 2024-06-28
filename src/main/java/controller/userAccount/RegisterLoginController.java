package controller.userAccount;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.exception.InternalServerError;
import common.exception.gen.UserNotFoundException;
import common.exception.login.WrongEmailPasswordException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.security.SensitiveData;
import common.record.LoginPayload;
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
import service.app.noAuthRequestService.login.LoginService;
import service.app.noAuthRequestService.register.RegisterService;

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

    /**
     * Controller for login. Returns a JWT token if the login is successful.
     * If the login is unsuccessful, it returns an error message.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The JWT token if the login is successful, or an error message.
     */
    @ApiOperation(value = "Login a user", response = String.class, tags = "Register and Login")
    @PostMapping("/user/login")
    public ResponseEntity<String> login(
        @ApiParam(value = "The email of the user.", required = true) @RequestParam String email,
        @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password) {
        try {
            LoginPayload tp = loginService.login(email, password);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            String prettyJson = gsonBuilder.create().toJson(tp);

            return ResponseEntity.ok(prettyJson);
        } catch (UserNotFoundException e) {
            // Redirect to register
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password");
        } catch (WrongEmailPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password");
        } catch (InternalServerError e) {
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
            JsonObject jwt = registerService.addUser(first_name, last_name, email, password, dob);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            String prettyJson = gsonBuilder.create().toJson(jwt);

            return ResponseEntity.ok(prettyJson);

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
