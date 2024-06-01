package controller;

import exception.login.WrongEmailPasswordException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import object.security.SensitiveData;
import okhttp3.Response;
import org.apache.http.protocol.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.LoginService;

import java.util.Map;

@Api(value = "LoginController")
@Controller
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The password of the user", required = true) @RequestParam SensitiveData password,
            @ApiParam(value = "lang", defaultValue = "fr") @RequestParam String lang
    ) {

        LoginService loginService = new LoginService();

        try {
            Map<String, String> s = loginService.login(email, password);
            return ResponseEntity.ok(s.get("token"));
        } catch (WrongEmailPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

    }

}
