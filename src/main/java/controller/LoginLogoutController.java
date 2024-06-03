package controller;

import common.exception.login.WrongEmailPasswordException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import common.object.security.SensitiveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.LoginLogoutService;
import com.google.gson.Gson;

import java.util.Map;

@Api(value = "Login-Logout-Controller")
@Controller
public class LoginLogoutController {

    /** Logger for the LoginController class */
    private static final Logger logger = LoggerFactory.getLogger(LoginLogoutController.class);

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The password of the user", required = true) @RequestParam SensitiveData password,
            @ApiParam(value = "lang", defaultValue = "fr") @RequestParam String lang
    ) {

        LoginLogoutService loginLogoutService = new LoginLogoutService();

        try {
            Map<String, String> s = loginLogoutService.login(email, password);
            Gson gson = new Gson();
            String json = gson.toJson(s);
            return ResponseEntity.ok(json);
        } catch (WrongEmailPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong email or password");
        } catch (Exception e) {
            logger.error("An error occurred while logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @ApiParam(value = "The ID of the user", required = true) @RequestParam int uid
    ) {

            LoginLogoutService loginLogoutService = new LoginLogoutService();

            try {
                loginLogoutService.logout(uid);
                return ResponseEntity.ok("Logged out");
            } catch (Exception e) {
                logger.error("An error occurred while logging out", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
    }

}
