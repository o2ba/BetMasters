package service.general.authService.refreshTokenService;

import common.object.security.EncryptedData;
import common.object.security.NonSensitiveData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.general.authService.refreshTokenService.dao.RefreshTokenDao;
import service.general.authService.refreshTokenService.token.RefreshTokenGenerator;

import java.sql.SQLException;
import java.time.LocalDateTime;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenDao refreshTokenDao;

    @Autowired
    public RefreshTokenServiceImpl(
            RefreshTokenGenerator refreshTokenGenerator,
            RefreshTokenDao refreshTokenDao
    ) {
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.refreshTokenDao = refreshTokenDao;
    }

    /**
     * Issue a new refresh token.
     * This includes generating a new refresh token, and encrypting it to the database. The unencrypted token is
     * returned.
     * @param uid The user id.
     * @param lifetime The lifetime of the refresh token.
     * @return The new refresh token (unencrypted).
     * @throws SQLException If the operation fails.
     */
    @Override
    public String issueRefreshToken(int uid, Long lifetime) throws SQLException {
        // Generate the refresh token.
        NonSensitiveData refreshToken = refreshTokenGenerator.generateRefreshToken();
        // Encrypt the refresh token and save it to the database.
        EncryptedData encryptedRefreshToken = refreshToken.encrypt();
        // Save the encrypted refresh token to the database.
        refreshTokenDao.saveRefreshToken(uid, encryptedRefreshToken.toString(), expDate(lifetime), halfExpDate(lifetime));
        // Return the unencrypted refresh token.
        return refreshToken.toString();
    }

    /**
     * Revoke a refresh token.
     * This includes deleting the refresh token from the database.
     * @param refreshToken The refresh token to revoke.
     * @throws SQLException If the operation fails.
     */
    @Override
    public void revokeRefreshToken(String refreshToken) throws SQLException {
        refreshTokenDao.deleteRefreshToken(refreshToken);
    }

    /**
     * Gets the age group of a refresh token.
     * Recommendation on how to handle respective refresh tokens:
     * MINOR: A new JWT token can be issued if necessary.
     * ADULT: A new JWT token can be issued if necessary.
     * A new refresh token should be issued. A refresh token is considered ADULT if it is more than half its lifetime.
     * DEAD: The refresh token is no longer valid. The user should be logged out.
     * @param refreshToken The refresh token.
     * @return The age group of the refresh token.
     */
    @Override
    public RefreshTokenAgeGroup getRefreshTokenAgeGroup(String refreshToken) {
        // Get the refresh token from the database.
        RefreshTokenDao.RefreshToken refreshToken1 =  refreshTokenDao.getRefreshToken(refreshToken);
        // Calculate the age of the refresh token.
        if (refreshToken1.expDate().isBefore(LocalDateTime.now())) {
            return RefreshTokenAgeGroup.DEAD;
        } else if (refreshToken1.halfDate().isBefore(LocalDateTime.now())) {
            return RefreshTokenAgeGroup.ADULT;
        } else {
            return RefreshTokenAgeGroup.MINOR;
        }
    }

    private LocalDateTime expDate(Long lifetime) {
        return LocalDateTime.now().plusSeconds(lifetime / 1000);
    }

    private LocalDateTime halfExpDate(Long lifetime) {
        return LocalDateTime.now().plusSeconds(lifetime / 2000);
    }
}
