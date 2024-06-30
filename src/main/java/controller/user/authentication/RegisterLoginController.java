package controller.user.authentication;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.exception.UnhandledErrorException;
import common.security.SensitiveData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.user.account.login.LoginService;
import service.app.user.account.login.exception.LoginDeniedException;
import service.app.user.account.register.RegisterService;
import service.app.user.account.register.exception.DuplicateEmailException;
import service.app.user.account.register.exception.InvalidAgeException;
import service.app.user.account.register.exception.InvalidInputException;

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
            @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password) throws UnhandledErrorException, LoginDeniedException {
        LoginService.LoginPayload tp = loginService.login(email, password);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        String prettyJson = gsonBuilder.create().toJson(tp);
        return ResponseEntity.ok(prettyJson);
    }

    @ApiOperation(value = "Register a new user", response = String.class, tags = "Register and Login")
    @PostMapping("/user/register")
    public ResponseEntity<String> register(
            @ApiParam(value = "The first name of the user.", required = true) @RequestParam String first_name,
            @ApiParam(value = "The last name of the user.", required = true) @RequestParam String last_name,
            @ApiParam(value = "The email of the user.", required = true) @RequestParam String email,
            @ApiParam(value = "The password of the user.", required = true) @RequestParam SensitiveData password,
            @ApiParam(value = "The date of birth of the user (YYYY-MM-DD).", required = true) @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob) throws InvalidAgeException, InvalidInputException, DuplicateEmailException, UnhandledErrorException {
        JsonObject tp = registerService.addUser(first_name, last_name, email, password, dob);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        String prettyJson = gsonBuilder.create().toJson(tp);
        return ResponseEntity.ok(prettyJson);
    }
}
