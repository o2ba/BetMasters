package service.general.external.dbRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * This interface defines methods for executing SQL queries, managing transactions, and handling database operations.
 * Implementations of this interface provide functionality to interact with a database.
 */
public interface DbRequest {

    /**
     * Executes an SQL query that does not return a result set.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query.
     * @return The number of rows affected by the query.
     * @throws SQLException If an error occurs while executing the query.
     */
    int execute(String query, Object... params) throws SQLException;

    /**
     * Executes an SQL query that returns a result set.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query.
     * @return A list of maps representing the result set. Each map corresponds to a row,
     *         where keys are column names and values are column values.
     * @throws SQLException If an error occurs while executing the query.
     */
    List<Map<String, Object>> query(String query, Object... params) throws SQLException;

    /**
     * Begins a transaction. All subsequent operations until commit or rollback are part of the same transaction.
     *
     * @throws SQLException If an error occurs while beginning the transaction.
     */
    void beginTransaction() throws SQLException;

    /**
     * Adds an operation to the current transaction. The operation is typically an SQL query or update.
     *
     * @param query  The SQL query to add to the transaction.
     * @param params The parameters to bind to the query.
     * @throws SQLException If an error occurs while adding the operation.
     */
    void addOperationToTransaction(String query, Object... params) throws SQLException;

    /**
     * Commits the current transaction, making all changes made since the transaction began permanent.
     *
     * @throws SQLException If an error occurs while committing the transaction.
     */
    void commitTransaction() throws SQLException;

    /**
     * Rolls back the current transaction, discarding all changes made since the transaction began.
     *
     * @throws SQLException If an error occurs while rolling back the transaction.
     */
    void rollbackTransaction() throws SQLException;
}
