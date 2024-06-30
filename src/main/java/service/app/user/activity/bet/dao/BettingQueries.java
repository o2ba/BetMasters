package service.app.user.activity.bet.dao;

public enum BettingQueries {
    PLACE("INSERT INTO bets (uid, fixtureId, bet_amount, bet_type, selected_bet, odds, status)" +
            " VALUES (?, ?, ?, ?, ?, ?, 'PENDING') RETURNING bet_id"),
    GET_BETS_FOR_USER("SELECT * FROM bets WHERE uid = ?"),
    GET_BET_BY_ID("SELECT * FROM bets WHERE bet_id = ?"),
    GET_BET("SELECT * FROM bets WHERE uid = ? AND bet_id = ?"),
    DELETE_BET("DELETE FROM bets WHERE uid = ? AND bet_id = ?"),
    CHANGE_STATUS("UPDATE bets SET status = ? WHERE bet_id = ?"),
    GET_TOTAL_AMOUNT_PENDING("SELECT SUM(bet_amount) FROM bets WHERE uid = ? AND status = 'PENDING'"),
    GET_TOTAL_AMOUNT_WON("SELECT SUM(bet_amount * odds) FROM bets WHERE uid = ? AND status = 'WON'"),
    GET_TOTAL_AMOUNT_LOST("SELECT SUM(bet_amount) FROM bets WHERE uid = ? AND status = 'LOST'");

    private final String query;

    BettingQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}