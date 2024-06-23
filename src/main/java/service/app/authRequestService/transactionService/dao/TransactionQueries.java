package service.app.authRequestService.transactionService.dao;

/**
 * Enum containing all the queries for the transaction table
 */
public enum TransactionQueries {
    /** The user deposits money into their account */
    DEPOSIT("INSERT INTO transactions (user_id, amount, transaction_type, sign) VALUES (?, ?, 'DEPOSIT', '+') RETURNING id"),

    /** The user withdraws money from their account */
    WITHDRAW("INSERT INTO transactions (user_id, amount, transaction_type, sign) VALUES (?, ?, 'WITHDRAW', '-') RETURNING id"),

    /** The user wins money */
    WIN("INSERT INTO transactions (user_id, amount, transaction_type, sign) VALUES (?, ?, 'WIN', '+') RETURNING id"),

    /** The user loses money */
    LOSE("INSERT INTO transactions (user_id, amount, transaction_type, sign) VALUES (?, ?, 'LOSE', '-') RETURNING id"),

    /** The user transfers money to another user */
    TRANSFER("INSERT INTO transactions (user_id, amount, transaction_type, sign) VALUES (?, ?, 'TRANSFER', '-') " +
            "RETURNING id"),

    /** The user receives money from another user */
    RECEIVE("INSERT INTO transactions (user_id, amount, transaction_type, sign) VALUES (?, ?, 'RECIEVE', '+') " +
            "RETURNING id");


    private final String query;

    TransactionQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
