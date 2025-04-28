package com.mindproapps.jira.integracaoponto.database;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;

import javax.sql.DataSource;
import java.sql.*;

@Log4j
public class LegacySQLProcessor implements AutoCloseable {
    private final Connection connection;
    private PreparedStatement statement;

    public LegacySQLProcessor(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public void prepareStatement(String sql) throws SQLException {
        this.statement = connection.prepareStatement(sql);
    }

    public void setValue(Object value) throws SQLException {
        if (statement == null) {
            throw new SQLException("PreparedStatement is not initialized.");
        }
        int nextIndex = statement.getParameterMetaData().getParameterCount() - getRemainingParameterCount() + 1;
        statement.setObject(nextIndex, value);
    }

    private int getRemainingParameterCount() throws SQLException {
        int count = 0;
        for (int i = 1; i <= statement.getParameterMetaData().getParameterCount(); i++) {
            try {
                statement.getParameterMetaData().getParameterType(i);
            } catch (SQLException e) {
                count++;
            }
        }
        return count;
    }

    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    @Override
    public void close() throws SQLException {
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }
}
