package service.app.user.account.register;

import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import common.exception.UnhandledErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.app.user.account.register.exception.DuplicateEmailException;
import common.security.SensitiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.general.internal.authService.jwtTokenService.JwtTokenService;
import service.app.user.account.register.dao.RegisterServiceDao;
import service.app.user.account.register.exception.InvalidAgeException;
import service.app.user.account.register.exception.InvalidInputException;
import service.general.internal.validation.Validation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public final class RegisterServiceImpl implements RegisterService {

    private final JwtTokenService jwtTokenService;
    private final RegisterServiceDao registerServiceDao;
    private final Validation validation;

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Value("${jwt.token.lifetime}")
    private Long jwtTokenLifetime;

    @Value("${user.minimum.age}")
    private int minimumAge;

    @Autowired
    public RegisterServiceImpl(JwtTokenService jwtTokenService, RegisterServiceDao registerServiceDao, Validation validation) {
        this.jwtTokenService = jwtTokenService;
        this.registerServiceDao = registerServiceDao;
        this.validation = validation;
    }

    @Override
    public JsonObject addUser(String firstName, String lastName, String email, SensitiveData password, LocalDate dob)
    throws DuplicateEmailException, InvalidAgeException, InvalidInputException, UnhandledErrorException {

        /* Validate user details */
        validateUserDetails(firstName, lastName, email, password, dob);

        /* Check that the user is old enough (set in config) */
        checkValidAge(dob);

        /* Add user to the database */
        int uid = registerServiceDao.addUser(firstName, lastName, email, password, dob, LocalDateTime.now());

        /* Generate JWT token */
        String newJwtToken = generateJwtToken(email, uid);

        return buildResponse(uid, email, newJwtToken);
    }

    private void validateUserDetails(String firstName, String lastName, String email, SensitiveData password, LocalDate dob)
            throws InvalidInputException, InvalidAgeException {
        if (!validation.isNameValid(firstName) || !validation.isNameValid(lastName)) {
            throw new InvalidInputException(InvalidInputException.ValidationFailureType.NAME);
        } else if (!validation.isEmailValid(email)) {
            throw new InvalidInputException(InvalidInputException.ValidationFailureType.EMAIL);
        } else if (!validation.isPasswordValid(password)) {
            throw new InvalidInputException(InvalidInputException.ValidationFailureType.PASSWORD);
        } else if (!validation.isDobValid(dob)) {
            throw new InvalidAgeException(InvalidAgeException.AgeFailureType.INVALID);
        }
    }

    private void checkValidAge(LocalDate dob) throws InvalidAgeException {
        if (dob.plusYears(minimumAge).isAfter(LocalDate.now())) {
            throw new InvalidAgeException(InvalidAgeException.AgeFailureType.TOO_YOUNG);
        }
    }

    private String generateJwtToken(String email, int uid) throws UnhandledErrorException {
        try {
            return jwtTokenService.generateEncryptedToken(email, uid, jwtTokenLifetime);
        } catch (JOSEException e) {
            logger.error("Error generating jwt token on user registration", e);
            throw new UnhandledErrorException("Error generating jwt token on user registration");
        }
    }

    private JsonObject buildResponse(int uid, String email, String newJwtToken) {
        JsonObject response = new JsonObject();
        response.addProperty("jwtToken", newJwtToken);
        response.addProperty("uid", uid);
        response.addProperty("email", email);
        return response;
    }
}
