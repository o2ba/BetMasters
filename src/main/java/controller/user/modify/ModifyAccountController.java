package controller.user.modify;

import com.google.gson.GsonBuilder;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.user.account.modify.ModifyAccountService;
import service.general.internal.authService.AuthorizationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Modify Account")
public class ModifyAccountController {

    Logger logger = LoggerFactory.getLogger(ModifyAccountController.class);

    ModifyAccountService modifyAccountService;
    AuthorizationService authorizationService;

    @Autowired
    public ModifyAccountController(ModifyAccountService modifyAccountService, AuthorizationService authorizationService) {
        this.modifyAccountService = modifyAccountService;
        this.authorizationService = authorizationService;
    }

    @DeleteMapping("/user/modify/delete")
    public ResponseEntity<String> deleteUser(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid)
            throws UserNotFoundException, UnhandledErrorException, NotAuthorizedException, InternalServerError {

        authorizationService.authorizeRequest(jwtToken, uid, email);

        modifyAccountService.deleteAccount(uid);

        Map<String, String> r = new HashMap<>();

        r.put("message", "User deleted successfully");
        r.put("uid", String.valueOf(uid));

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String prettyJson = gsonBuilder.create().toJson(r);

        return ResponseEntity.status(200).body(prettyJson);
    }
}
