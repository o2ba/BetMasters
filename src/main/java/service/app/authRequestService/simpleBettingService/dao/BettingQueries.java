package service.app.authRequestService.simpleBettingService.dao;

public enum BettingQueries {
    PLACE("INSERT INTO bets (uid, fixtureId, bet_amount, bet_type, selected_bet, odds)" +
            " VALUES (?, ?, ?, ?, ?, ?)"),
    GET_BETS("SELECT * FROM bets WHERE uid = ?"),
    GET_BET("SELECT * FROM bets WHERE uid = ? AND id = ?"),
    DELETE_BET("DELETE FROM bets WHERE uid = ? AND id = ?");

    private final String query;

    BettingQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}