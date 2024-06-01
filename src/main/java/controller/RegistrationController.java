package controller;

import exception.gen.RateLimitException;
import exception.gen.UserNotFoundException;
import exception.register.DuplicateEmailException;
import exception.register.ExpiredTokenException;
import exception.register.InvalidTokenException;
import exception.register.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import object.security.SensitiveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import service.RegistrationService;
import util.spring.SpringLanguageUtil;

import java.sql.SQLException;
import java.time.LocalDate;

@Api(value = "FixtureBetsController", description = "FixtureBetsController")
@Controller
public class RegistrationController {

    /** logger for the RegistrationController class */
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final SpringLanguageUtil langUtil;

    public RegistrationController(MessageSource messageSource) {
        this.langUtil = new SpringLanguageUtil(messageSource);
    }

    /**
     * Registers a new user. Password is hashed from frontend,
     * as well as from the backend. The frontend must verify
     * the password (2x entry), as well as require a box
     * to be checked for privacy policy
     * @param email the email of the user
     * @param password the password of the user
     */
    @PostMapping("/registerUser")
    public ResponseEntity<String> register(
            @ApiParam(value = "firstName") @RequestParam String firstName,
            @ApiParam(value = "lastName") @RequestParam String lastName,
            @ApiParam(value = "email") @RequestParam String email,
            @ApiParam(value = "Password 8-50 chars") @RequestParam SensitiveData password,
            @ApiParam(value = "Language code", example = "en", defaultValue = "en") @RequestHeader String language,
            @ApiParam(value = "Date of birth in YYYY-MM-DD format", example = "2002-06-16")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date

    ) {
        RegistrationService registrationService = new RegistrationService();
        try {
            registrationService.createUser(firstName, lastName, email, password, date);
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(409).body(langUtil.getMessage("email.exists", language));
        } catch (RateLimitException e) {
            return ResponseEntity.status(429).body(langUtil.getMessage("rate.limit", language));
        } catch (SQLException e) {
            logger.error("SQL Exception: Unable to create account", e);
            return ResponseEntity.status(429).body(langUtil.getMessage("unable.create.account", language));
        } catch (ValidationException e) {
            logger.warn("Validation Failure in Backend! Injection Attack?");
            return ResponseEntity.status(422).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unknown Exception: Unable to create account", e);
            return ResponseEntity.status(422).body(
                    langUtil.getMessage("unable.create.account", language)
            );
        }

        return ResponseEntity.status(201).body(
                langUtil.getMessage("user.created", language)
        );
    }


    /**
     * Verifies the email of a user
     * @param emailToken the token sent to the user's email
     */
    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(
            @ApiParam(value = "token") @RequestParam String emailToken,
            @ApiParam(value = "Language code", example = "en", defaultValue = "en") @RequestHeader String language
    ) {
        RegistrationService registrationService = new RegistrationService();
        try {
            registrationService.verifyEmail(emailToken);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(422).body(
                    langUtil.getMessage(UserNotFoundException.MESSAGE_ID_TOKEN, language));
        } catch (InvalidTokenException e2) {
            return ResponseEntity.status(422).body(
                    langUtil.getMessage(InvalidTokenException.messageId, language));
        } catch (ExpiredTokenException e3) {
            return ResponseEntity.status(422).body(
                    langUtil.getMessage(ExpiredTokenException.MESSAGE_ID, language));
        } catch (SQLException e) {
            logger.error("SQL Exception: Unable to verify email", e);
            return ResponseEntity.status(422).body(
                    langUtil.getMessage("email.verification.failure", language));
        } catch (Exception e) {
            logger.error("Unknown Exception: Unable to verify email", e);
            return ResponseEntity.status(422).body(
                    langUtil.getMessage("email.verification.failure", language)
            );
        }

        return ResponseEntity.status(201).body(
                langUtil.getMessage("email.verification.success", language)
        );
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteAccount(
            @ApiParam(value = "uid") @RequestParam int uid,
            @ApiParam(value = "Language code", example = "en", defaultValue = "en") @RequestHeader String language
    ) {
        RegistrationService registrationService = new RegistrationService();
        try {
            registrationService.deleteUser(uid);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(422).body(
                    langUtil.getMessage(UserNotFoundException.MESSAGE_ID, language));
        } catch (SQLException e) {
            logger.error("SQL Exception: Unable to delete account", e);
            return ResponseEntity.status(422).body(
                    langUtil.getMessage("unable.delete.account", language));
        } catch (Exception e) {
            logger.error("Unknown Exception: Unable to delete account", e);
            return ResponseEntity.status(422).body(
                    langUtil.getMessage("unable.delete.account", language)
            );
        }

        return ResponseEntity.status(201).body(
                langUtil.getMessage("user.deleted", language)
        );
    }

    // TODO Reset password

}
