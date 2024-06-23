package dto.request;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PostgresRequest is a class that handles database operations using HikariCP for connection pooling.
 * It provides methods to execute SQL queries and updates, and manages the database connection pool efficiently.
 * This class is code-injection safe as it uses prepared statements to prevent SQL injection attacks.
 */
@Service
public final class PostgresRequest {

    /** Logger for the PostgresRequest class */
    private static final Logger logger = LoggerFactory.getLogger(PostgresRequest.class);

    /** The username for the database. */
    private static final String USERNAME = System.getenv("DB_USERNAME");

    /** The password for the database. */
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    /** The URL of the database. The URL must contain the port. */
    private static final String URL = System.getenv("DB_URL");

    /** HikariCP DataSource for connection pooling. */
    private static HikariDataSource dataSource;


    // ----- Database Credentials and Connection Pool Initialization ----- //

    /*
     * This block ensures that the necessary credentials are loaded from environment variables
     * and the HikariCP connection pool is set up with these credentials.
     */
    static {
        getAuthDetails();
        setupDataSource();
    }

    // ------------------------------------------------------------------- //

    /**
     * Constructor for the PostgresRequest class.
     * <p>
     * Ensures that the HikariCP DataSource has been initialized.
     * </p>
     *
     * @throws IllegalStateException if the DataSource is not initialized.
     */
    public PostgresRequest() {
        if (dataSource == null) throw new RuntimeException("DataSource not initialized.");
    }

    /**
     * Retrieves the authentication details from environment variables.
     * <p>
     * Reads the database username, password, URL, and database name from environment variables.
     * If any of these variables are not set, an IllegalStateException is thrown.
     * </p>
     *
     * @throws IllegalStateException if any of the required environment variables are not set.
     */
    private static void getAuthDetails() throws IllegalStateException {
        if (USERNAME == null || PASSWORD == null || URL == null) {
            logger.error("Environment variables not set for database authentication.");
            throw new IllegalStateException("Environment variables not set for database authentication.");
        } else {
            logger.debug("Database authentication details retrieved successfully.");
        }
    }

    /**
     * Sets up the HikariCP DataSource with the necessary configuration parameters.
     * <p>
     * This method initializes the HikariCP connection pool using the database credentials
     * retrieved from the environment variables. The following configurations are set:
     * </p>
     * <ul>
     *     <li><b>JDBC URL</b>: The full JDBC URL for connecting to the database, which includes
     *     the database URL and name.</li>
     *     <li><b>Username</b>: The username for authenticating with the database.</li>
     *     <li><b>Password</b>: The password for authenticating with the database.</li>
     *     <li><b>Cache Prepared Statements</b>: Enables caching of prepared statements for
     *     improved performance. This reduces the overhead of preparing statements repeatedly.</li>
     *     <li><b>Prepared Statement Cache Size</b>: Sets the maximum number of prepared statements
     *     that can be cached. This helps manage memory usage while maintaining performance.</li>
     *     <li><b>Prepared Statement Cache SQL Limit</b>: Sets the maximum length of SQL statements
     *     that can be cached. This ensures that only reasonably sized SQL statements are cached.</li>
     * </ul>
     * <p>
     * If the DataSource is successfully initialized, a confirmation message is logged.
     * In case of any errors during initialization, an error message is logged.
     * </p>
     */
    private static void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        logger.info("HikariCP DataSource initialized successfully.");
    }

    /**
     * Prepares a statement for execution. The purpose of this method is to suppress
     * the warning generated by the unchecked call to prepareStatement.
     * @param conn the connection to the database
     * @param sql the SQL query to be prepared
     * @return the prepared statement
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    /**
     * Executes a SQL query and returns the ResultSet.
     * <p>
     * This method prepares a SQL query using the provided parameters, executes it, and
     * returns the ResultSet containing the query results.
     * </p>
     *
     * @param query the SQL query to be executed.
     * @param params the parameters for the prepared statement.
     * @return the ResultSet containing the query results.
     * @throws SQLException if a database access error occurs.
     */
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = prepareStatement(connection, query);
            setParameters(stmt, params);
            return stmt.executeQuery();
        } catch (SQLException e) {
            logger.error("Query operation failed: {}", e.getMessage());
            logger.debug("Query SQLException details: SQLState={}, ErrorCode={}", e.getSQLState(), e.getErrorCode());
            throw e;
        }
    }

    public List<Map<String, Object>> safeExecuteQuery(String query, Object... params) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = prepareStatement(connection, query)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("Query operation failed: {}", e.getMessage());
            logger.debug("Query SQLException details: SQLState={}, ErrorCode={}", e.getSQLState(), e.getErrorCode());
            throw e;
        }

        return resultList;
    }

    /**
     * Executes a SQL update and returns the number of affected rows.
     * <p>
     * This method prepares a SQL update statement using the provided parameters,
     * executes it, and returns the number of rows affected by the update.
     * </p>
     *
     * @param query the SQL update query to be executed.
     * @param params the parameters for the prepared statement.
     * @return the number of rows affected by the update.
     * @throws SQLException if a database access error occurs.
     */
    public int executeUpdate(String query, Object... params) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = prepareStatement(connection, query)) {
                setParameters(stmt, params);
                return stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Update operation failed: {}", e.getMessage());
            logger.debug("Update SQLException details: SQLState={}, ErrorCode={}", e.getSQLState(), e.getErrorCode());
            throw e;
        }
    }

    /**
     * Sets the parameters for a prepared statement.
     * <p>
     * This method sets the provided parameters for a prepared SQL statement.
     * </p>
     *
     * @param stmt the prepared statement.
     * @param params the parameters to set.
     * @throws SQLException if a database access error occurs.
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    /**
     * Closes the HikariCP DataSource.
     * <p>
     * This method closes the HikariCP DataSource, releasing all resources.
     * </p>
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource closed.");
        }
    }
}