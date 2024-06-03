package service.app.userService.publicRequest.login;

import common.exception.login.WrongEmailPasswordException;
import common.object.security.SensitiveData;
import service.general.__deprecated.user.ValidationUtil;
import service.general.__deprecated.tokenService.TokenPayload;

import java.sql.SQLException;

public class LoginService {

    private final UserLogin userLogin;

    /** Constructor for the LoginService class */
    public LoginService(UserLogin userLogin, ValidationUtil validationUtil) {
        this.userLogin = userLogin;

    }

    public TokenPayload.UserTokenPayload login(String email, SensitiveData password)
    throws SQLException, WrongEmailPasswordException
    {
        // Validate the email

        // Get the hashed password and uid from the database
        UserLogin.PassWordUIDPayload passWordUIDPayload = userLogin.getHashedPasswordForUser(email);

        // Check if the email is not found
        if(passWordUIDPayload == null) {
            throw new WrongEmailPasswordException();
        }

        // Check if the password matches the hashed password in the database
        if (!userLogin.passwordMatches(password, passWordUIDPayload)) {
            throw new WrongEmailPasswordException();
        }

        // Issue a JWT and a fresh refresh token for the user
        TokenPayload.UserTokenPayload userTokenPayload = userLogin.issueTokens(passWordUIDPayload.uid());


        return userTokenPayload;
    }

}
