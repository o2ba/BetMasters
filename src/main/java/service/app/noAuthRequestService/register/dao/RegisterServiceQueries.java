package service.app.noAuthRequestService.register.dao;

public enum RegisterServiceQueries {
    ADD_USER("INSERT INTO users (first_name, last_name, email, password, dob, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING uid");

    private final String query;

    RegisterServiceQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
