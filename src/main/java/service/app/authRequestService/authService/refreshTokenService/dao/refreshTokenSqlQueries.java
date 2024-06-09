package service.app.authRequestService.authService.refreshTokenService.dao;

public enum refreshTokenSqlQueries {
    GET_REFRESH_TOKENS_BY_UID("SELECT * FROM refresh_tokens WHERE uid = ?"),
    GET_REFRESH_TOKEN("SELECT * FROM refresh_tokens WHERE token = ?"),
    SAVE_REFRESH_TOKEN("INSERT INTO refresh_tokens (uid, token, expiry_date, halftime_date) VALUES (?, ?, ?, " +
            "?)"),
    DELETE_REFRESH_TOKEN_BY_UID("DELETE FROM refresh_tokens WHERE uid = ?"),
    DELETE_REFRESH_TOKEN("DELETE FROM refresh_tokens WHERE token = ?");

    private final String query;

    refreshTokenSqlQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
