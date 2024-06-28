package controller.userAccount;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.security.SensitiveData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.authRequestService.modifyAccount.ModifyAccount;

@RestController
@Api(tags = "Modify Account")
public class ModifyAccountController {

    ModifyAccount modifyAccount;

    @Autowired
    public ModifyAccountController(ModifyAccount modifyAccount) {
        this.modifyAccount = modifyAccount;
    }

    @PatchMapping("/user/modify/email")
    public ResponseEntity<String> changeEmail(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid,
            @ApiParam(value = "The new email", required = true) @RequestParam String newEmail
    ) {
        try {
            JsonElement modified = modifyAccount.changeEmail(jwtToken, uid, email, newEmail);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            String prettyJson = gsonBuilder.create().toJson(modified);

            return ResponseEntity.ok(prettyJson);
        } catch (InternalServerError e) {
            return ResponseEntity.badRequest().body("Internal server error");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.badRequest().body("Not authorized");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.badRequest().body("Target email already exists");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/user/modify/password")
    public ResponseEntity<String> changePassword(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid,
            @ApiParam(value = "The new password", required = true) @RequestParam SensitiveData newPassword
    ) {
        try {
            modifyAccount.changePassword(jwtToken, uid, email, newPassword);
        } catch (InternalServerError e) {
            return ResponseEntity.badRequest().body("Internal server error");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.badRequest().body("Not authorized");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Validation error");
        }
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/user/modify/delete")
    public ResponseEntity<String> deleteAccount(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid
    ) {
        try {
            modifyAccount.deleteAccount(jwtToken, uid, email);
        } catch (InternalServerError e) {
            return ResponseEntity.badRequest().body("Could not delete email. Please try again later.");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.badRequest().body("Not authorized");
        }
        return ResponseEntity.ok("Account deleted successfully");
    }

}
