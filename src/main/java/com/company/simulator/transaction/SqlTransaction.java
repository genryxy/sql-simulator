package com.company.simulator.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class SqlTransaction {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlTransaction(
            @Value("${db.garbage.url}") String url,
            @Value(value = "${db.garbage.username}") String username,
            @Value(value = "${db.garbage.password}") String password
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource(url, username, password));
    }

    private DataSource dataSource(String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    public ResponseEntity executeQuery(String dslScript, String studentSelect, String correctSelect) {

        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            connection.setAutoCommit(false);
            jdbcTemplate.update(getScriptDeleteTables());
            jdbcTemplate.update(dslScript);
            //jdbcTemplate.query(studentSelect + correctSelect, ); need mapper
            //TODO compare results of requests
            connection.commit();
            return new ResponseEntity(HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getScriptDeleteTables() {
        String script = String.join("\n", jdbcTemplate.query(
                "SELECT 'drop table if exists \"' || tablename || '\" cascade;' as pg_tbl_drop\n" +
                        "FROM pg_tables\n" +
                        "WHERE schemaname='public'",
                (rs, rowNum) -> rs.getString("pg_tbl_drop")));
        System.out.println(script); // debug print
        return script;
    }
}
