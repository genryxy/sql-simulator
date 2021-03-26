package com.company.simulator.sql;

import com.google.common.base.Throwables;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

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

    public ResponseEntity<ResultQuery> executeQuery(
        String dslScript, String studentQuery, String correctQuery
    ) {
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            connection.setAutoCommit(false);
            jdbcTemplate.update(getScriptDeleteTables());
            jdbcTemplate.update(dslScript);
            final List<Map<String, Object>> stud = jdbcTemplate.queryForList(studentQuery);
            final List<Map<String, Object>> corr = jdbcTemplate.queryForList(correctQuery);
            final boolean same = new QueryResultComparison(corr).compareWith(stud);
            connection.commit();
            return ResponseEntity.ok(new ResultQuery(same));
        } catch(final SQLException exc) {
            return logAngGetResponse(exc);
        } catch (Exception exc) {
            final Throwable cause = Throwables.getRootCause(exc);
            if (cause instanceof SQLException) {
                return logAngGetResponse((SQLException) cause);
            }
            final ResultQuery res = new ResultQuery(false);
            res.setInternalError(Optional.of(exc.getMessage()));
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(res);
        }
    }

    public void validationTeacherQuery(String dsl, String correctSelect){
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            connection.setAutoCommit(false);
            jdbcTemplate.update(getScriptDeleteTables());
            jdbcTemplate.update(dsl);
            jdbcTemplate.query(correctSelect, (rs, rowNum) -> null);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
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

    private ResponseEntity<ResultQuery> logAngGetResponse(SQLException exc) {
        LoggerFactory.getLogger(SqlTransaction.class)
            .error(String.format("SQL exception in transaction: %s", exc.getMessage()));
        final ResultQuery res = new ResultQuery(false);
        res.setSqlException(Optional.of(exc.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    /**
     * Wrapper class for result of executing student query.
     */
    @Getter
    @Setter
    public static class ResultQuery {
        /**
         * Is the result of query correct?
         */
        private boolean isCorrect;

        /**
         * Message of SQL exception in case of existence, empty otherwise.
         */
        private Optional<String> sqlException;

        /**
         * Message of internal exception in case of existence, empty otherwise.
         */
        private Optional<String> internalError;

        public ResultQuery(boolean isCorrect) {
            this.isCorrect = isCorrect;
            this.sqlException = Optional.empty();
            this.internalError = Optional.empty();
        }
    }
}
