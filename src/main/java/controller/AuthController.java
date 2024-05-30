package main.java.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import main.java.exception.sql.DuplicateEmailException;
import main.java.exception.sql.RateLimitException;
import main.java.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Api(value = "FixtureBetsController", description = "FixtureBetsController")
@Controller
public class AuthController {


    /**
     * Registers a new user. Password is hashed from frontend,
     * as well as from the backend. The frontend must verify
     * the password (2x entry), as well as require a box
     * to be checked for privacy policy
     * @param email the email of the user
     * @param password the password of the user
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @ApiParam(value = "firstName") String firstName,
            @ApiParam(value = "lastName") String lastName,
            @ApiParam(value = "email") String email,
            @ApiParam(value = "password") String password,
            @ApiParam(value = "dob") String dob

    ) {
        var authService = new AuthService();
        try {
            authService.createUser(firstName, lastName, email, password, dob);
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(409).body("Email already exists");
        } catch (RateLimitException e) {
            return ResponseEntity.status(429).body("Too many requests");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }

        return ResponseEntity.status(201).body("User Created");
    }


    /**
     * Verifies the email of a user
     * @param emailToken the token sent to the user's email
     */
    @PostMapping("/verifyEmail")
    public void verifyEmail(
            @ApiParam(value = "emailToken") String emailToken
    ) {

    }

    @PostMapping("/resetPassword")
    public void resetPassword(
            @ApiParam(value = "email") String email
    ) {


    }


    @PostMapping("/login")
    public void login(
            @ApiParam(value = "email") String email,
            @ApiParam(value = "password") String password
    ) {


    }



}
