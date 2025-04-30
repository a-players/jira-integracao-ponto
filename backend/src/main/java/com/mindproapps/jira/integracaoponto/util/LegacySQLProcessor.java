package com.mindproapps.jira.integracaoponto.util;

import java.sql.*;

public class LegacySQLProcessor implements AutoCloseable {

    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private int paramIndex = 1; // Gerencia os índices dos parâmetros.

    public LegacySQLProcessor(Connection connection) {
        this.connection = connection;
    }

    public void prepareStatement(String sql) throws SQLException {
        this.preparedStatement = connection.prepareStatement(sql);
        this.paramIndex = 1;
    }

    public void setValue(Object value) throws SQLException {
        if (preparedStatement == null) throw new SQLException("PreparedStatement not initialized");
        preparedStatement.setObject(paramIndex++, value);
    }

    public ResultSet executeQuery() throws SQLException {
        if (preparedStatement == null) throw new SQLException("PreparedStatement not initialized");
        this.resultSet = preparedStatement.executeQuery();
        return this.resultSet;
    }

    public int executeUpdate() throws SQLException {
        if (preparedStatement == null) throw new SQLException("PreparedStatement not initialized");
        return preparedStatement.executeUpdate();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        }
    }

    @Override
    public void close() throws Exception {
        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (connection != null) connection.close();
    }
}
