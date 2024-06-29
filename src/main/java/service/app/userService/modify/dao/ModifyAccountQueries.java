package service.app.userService.modify.dao;

public enum ModifyAccountQueries
{
    DELETE_ACCOUNT("DELETE FROM users WHERE uid = ?"),
    CHANGE_PASSWORD("UPDATE users SET password = ? WHERE uid = ?"),
    CHANGE_EMAIL("UPDATE users SET email = ? WHERE uid = ?");

    private final String query;

    private ModifyAccountQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
