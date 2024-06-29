package dto.dbRequest;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dto.dbRequest.exception.DbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@SuppressWarnings({"duplicate", "unused"})
public class DbRequestImpl implements DbRequest {

    private static final Logger logger = LoggerFactory.getLogger(DbRequestImpl.class);

    @Value("${database.username}")
    private String USERNAME;

    @Value("${database.password}")
    private String PASSWORD;

    @Value("${database.host}")
    private String HOST;

    @Value("${database.port}")
    private String PORT;

    @Value("${database.name}")
    private String DATABASE;

    private HikariDataSource dataSource;
    private Connection currentConnection;

    @PostConstruct
    public void init() {
        setupDataSource();
    }

    private void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    /**
     * Executes an SQL query that does not return a result set.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query.
     * @return The number of rows affected by the query.
     * @throws DbException If an error occurs while executing the query.
     */
    @Override
    @SuppressWarnings("all") // Supresses warnings for prepareStatement
    public int execute(String query, Object... params) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Executes an SQL query that returns a result set.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query.
     * @return A list of maps, where each map represents a row in the result set.
     * The keys in the map are the column names, and the values are the column values.
     * @throws DbException If an error occurs while executing the query.
     */
    @Override
    @SuppressWarnings("all")
    public List<Map<String, Object>> query(String query, Object... params) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
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
            throw e;
        }
        return resultList;
    }

    @Override
    public void beginTransaction() throws SQLException {
        try {
            getConnection().setAutoCommit(false); // Start transaction
        } catch (SQLException e) {
            throw new SQLException("Failed to start transaction", e);
        }
    }

    @Override
    public void addOperationToTransaction(String query, Object... params) throws SQLException {
        if (currentConnection == null || currentConnection.isClosed()) {
            throw new SQLException("No active transaction to add operation.");
        }
        PreparedStatement stmt = prepareStatement(query, params);
        stmt.executeUpdate();
    }

    @Override
    @SuppressWarnings("all")
    public void commitTransaction() throws SQLException {
        try {
            getConnection().commit(); // Commit transaction
        } catch (SQLException e) {
            throw new SQLException("Failed to commit transaction: " + e.getMessage(), e);
        } finally {
            try {
                getConnection().setAutoCommit(true); // Return to auto-commit mode
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public void rollbackTransaction() throws SQLException {
        try {
            getConnection().rollback(); // Rollback transaction
        } catch (SQLException e) {
            throw new SQLException("Failed to rollback transaction: " + e.getMessage(), e);
        } finally {
            try {
                getConnection().setAutoCommit(true); // Return to auto-commit mode
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        if (currentConnection == null || currentConnection.isClosed()) {
            throw new SQLException("Connection is closed or not initialized.");
        }
        PreparedStatement stmt = currentConnection.prepareStatement(query);
        setParameters(stmt, params);
        return stmt;
    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false); // Ensure transaction is started if not already
        }
        return connection;
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    // Other methods like transaction management (beginTransaction, commitTransaction, rollbackTransaction) can be added here

    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}