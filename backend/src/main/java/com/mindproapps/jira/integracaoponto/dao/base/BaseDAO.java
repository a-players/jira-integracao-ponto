package com.mindproapps.jira.integracaoponto.dao.base;

import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;
import lombok.extern.log4j.Log4j;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j
public class BaseDAO {

    protected LegacySQLProcessor createSQLProcessor() throws SQLException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/defaultDS");
            Connection connection = ds.getConnection();
            return new LegacySQLProcessor(connection);
        } catch (Exception e) {
            log.error("Erro ao obter conexão com o banco de dados", e);
            throw new SQLException("Erro ao obter conexão", e);
        }
    }
}
