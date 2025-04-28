package com.mindproapps.jira.integracaoponto.util;

import java.sql.*;

public class LegacySQLProcessor implements AutoCloseable {

    private final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private int paramIndex = 1; // Para gerenciar os índices dos parâmetros.

    public LegacySQLProcessor(Connection connection) {
        this.connection = connection;
    }

    // Prepara o statement com o SQL fornecido.
    public void prepareStatement(String sql) throws SQLException {
        this.preparedStatement = connection.prepareStatement(sql);
        paramIndex = 1; // Resetar o índice para os parâmetros toda vez que um novo SQL for preparado.
    }

    // Define o valor para o próximo parâmetro do statement
    public void setValue(Object value) throws SQLException {
        if (preparedStatement == null) throw new SQLException("PreparedStatement not initialized");

        preparedStatement.setObject(paramIndex++, value);  // Define o valor e incrementa o índice para o próximo parâmetro.
    }

    // Executa a query e retorna o ResultSet
    public ResultSet executeQuery() throws SQLException {
        if (preparedStatement == null) {
            throw new SQLException("PreparedStatement not initialized");
        }
        this.resultSet = preparedStatement.executeQuery();
        return this.resultSet;
    }

    // Executa um comando de update (INSERT, UPDATE, DELETE) e retorna o número de linhas afetadas.
    public int executeUpdate() throws SQLException {
        if (preparedStatement == null) {
            throw new SQLException("PreparedStatement not initialized");
        }
        return preparedStatement.executeUpdate();
    }

    // Executa um comando simples de consulta SQL sem parâmetros e retorna o ResultSet
    public ResultSet executeQuery(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        }
    }

    // Método para fechar a conexão, statement e resultSet
    @Override
    public void close() throws Exception {
        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (connection != null) connection.close();
    }
}
