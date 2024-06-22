package service.app.noAuthRequestService.login.dao;

public enum LoginServiceQueries {
    GET_USER_BY_EMAIL_RETURN_ID("SELECT * FROM users WHERE email = ?");

    private final String query;

    LoginServiceQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
