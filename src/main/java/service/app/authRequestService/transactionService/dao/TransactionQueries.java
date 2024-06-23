package service.app.authRequestService.transactionService.dao;


import org.springframework.stereotype.Component;
/**
 * Enum containing all the queries for the transaction table
 */
public enum TransactionQueries {
    /** The user deposits money into their account */
    DEPOSIT("INSERT INTO transactions (uid, amount, transaction_type, sign) VALUES (?, ?, 'DEPOSIT', '+')"),

    /** The user withdraws money from their account */
    WITHDRAW("INSERT INTO transactions (uid, amount, transaction_type, sign) VALUES (?, ?, 'WITHDRAW', '-')"),

    /** Money is added by the system. Used for example if the user wins */
    ADD_INTERNAL("INSERT INTO transactions (uid, amount, transaction_type, sign) VALUES (?, ?, ?, '+')"),

    /** Money is removed by the system. Used for example if the user loses */
    REMOVE_INTERNAL("INSERT INTO transactions (uid, amount, transaction_type, sign) VALUES (?, ?, ?, '-')"),

    /** The user transfers money to another user */
    TRANSFER("INSERT INTO transactions (uid, amount, transaction_type, sign) VALUES (?, ?, 'TRANSFER', '-')"),

    /** The user receives money from another user */
    RECEIVE("INSERT INTO transactions (uid, amount, transaction_type, sign) VALUES (?, ?, 'RECIEVE', '+')"),

    /** Get a list of all the transactions of a user */
    GET_TRANSACTIONS("SELECT * FROM transactions WHERE uid = ?"),

    /** Get the balance of a user */
    GET_BALANCE("SELECT SUM(amount) FROM transactions WHERE uid = ?");


    private final String query;

    TransactionQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
