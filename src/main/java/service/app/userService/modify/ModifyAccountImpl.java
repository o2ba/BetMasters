package service.app.userService.modify;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.DuplicateEmailException;
import common.exception.register.ValidationException;
import common.security.SensitiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.general.authService.AuthorizationService;
import service.general.authService.jwtTokenService.JwtTokenService;
import service.app.userService.modify.dao.ModifyAccountDao;
import service.general.validation.Validation;

@Service
public class ModifyAccountImpl implements ModifyAccount {


    @Value("${jwt.token.lifetime}")
    private Long jwtTokenLifetime;

    private final ModifyAccountDao modifyAccountDao;
    private final AuthorizationService authService;
    private final Validation validation;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public ModifyAccountImpl(ModifyAccountDao modifyAccountDao, AuthorizationService authService, Validation validation, JwtTokenService jwtTokenService) {
        this.modifyAccountDao = modifyAccountDao;
        this.authService = authService;
        this.validation = validation;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void deleteAccount(String jwtToken, int uid, String email) throws InternalServerError, NotAuthorizedException {
        authService.authorizeRequest(jwtToken, uid, email);

        int changesMade = modifyAccountDao.deleteAccount(jwtToken, uid, email);

        if (changesMade == 0) {
            throw new InternalServerError("Error deleting account");
        }

    }

    @Override
    public void changePassword(String jwtToken, int uid, String email, SensitiveData newPassword) throws InternalServerError, NotAuthorizedException, ValidationException {
        authService.authorizeRequest(jwtToken, uid, email);

        if (!validation.isPasswordValid(newPassword)) {
            throw new ValidationException("Password is not valid");
        }

        int changesMade = modifyAccountDao.changePassword(jwtToken, uid, email, newPassword);

        if (changesMade == 0) {
            throw new InternalServerError("Error updating password");
        }
    }

    @Override
    public JsonElement changeEmail(String jwtToken, int uid, String email, String newEmail) throws InternalServerError, NotAuthorizedException, ValidationException, DuplicateEmailException {
        authService.authorizeRequest(jwtToken, uid, email);

        if (email.equals(newEmail)) {
            throw new ValidationException("Email cannot be the same as the current email");
        }

        // Validate the new email
        if (!validation.isEmailValid(newEmail)) {
            throw new ValidationException("Email is not valid");
        }

        // Update the email in the database
        int changedMade = modifyAccountDao.changeEmail(jwtToken, uid, email, newEmail);

        if (changedMade == 0) {
            throw new InternalServerError("Error updating email");
        }

        String newJwtToken;

        try {
            newJwtToken = jwtTokenService.generateEncryptedToken(newEmail, uid, jwtTokenLifetime);
        } catch (JOSEException e) {
            throw new InternalServerError("Error generating new token");
        }

        JsonObject response = new JsonObject();
        response.addProperty("jwtToken", newJwtToken);
        response.addProperty("uid", uid);
        response.addProperty("email", newEmail);

        return response;
    }
}
