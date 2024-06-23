package service.general.authService.refreshTokenService.dao;

import dto.PostgresRequest;
import org.junit.jupiter.api.Test;
import service.app.authRequestService.authService.refreshTokenService.dao.RefreshTokenDao;
import service.app.authRequestService.authService.refreshTokenService.dao.RefreshTokenDaoImpl;
import service.app.authRequestService.authService.refreshTokenService.token.RefreshTokenGenerator;
import service.app.authRequestService.authService.refreshTokenService.token.RefreshTokenGeneratorImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenDaoImplTest {


    RefreshTokenDao refreshTokenDao = new RefreshTokenDaoImpl(new PostgresRequest());
    RefreshTokenGenerator refreshTokenGenerator = new RefreshTokenGeneratorImpl();

    @Test
    void getRefreshTokensByUID() throws Exception {
        // Generate 3 refresh tokens for user 30.
        String re1 = refreshTokenGenerator.generateRefreshToken().encrypt().toString();
        String re2 = refreshTokenGenerator.generateRefreshToken().encrypt().toString();
        String re3 = refreshTokenGenerator.generateRefreshToken().encrypt().toString();

        // Adds a refresh token to the database. Not mocked.
        refreshTokenDao.saveRefreshToken(30, re1, LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(15));
        refreshTokenDao.saveRefreshToken(30, re2, LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(15));
        refreshTokenDao.saveRefreshToken(30, re3, LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(15));

        // Get the refresh tokens for user 30.
        assertEquals(3, refreshTokenDao.getRefreshTokensByUID(30).size());

    }

    @Test
    void getRefreshToken() {
    }

    @Test
    void saveRefreshToken() throws Exception {
        // Generate a refresh token.
        String re = refreshTokenGenerator.generateRefreshToken().encrypt().toString();

        // Adds a refresh token to the database. Not mocked.
        refreshTokenDao.saveRefreshToken(30, re, LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(15));
    }

    @Test
    void deleteRefreshTokenByUID() {
    }

    @Test
    void deleteRefreshToken() {
    }
}
