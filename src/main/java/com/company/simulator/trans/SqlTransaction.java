package com.company.simulator.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SqlTransaction implements AutoCloseable{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlTransaction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity executeQuery(String sql) {
        try {
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String[] wordsFromSQL = sql.split(" +"); // на случай более 1 пробела

            if (checkingTableNames(wordsFromSQL)) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else {
                connection.setAutoCommit(false);
                jdbcTemplate.update(sql);
                List<String> tableList = getTablesFromSQL(wordsFromSQL);
                for (String tableName : tableList) {
                    jdbcTemplate.update("drop table if exists " + tableName);
                }
                connection.commit();
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.valueOf(500));
        }
    }

    private List<String> getTablesFromSQL(String[] words) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("table") && i < words.length + 1) {
                result.add(words[i + 1]);
            }
        }
        return result;
    }

    private boolean checkingTableNames(String[] tables) {
        List<String> ourTables = jdbcTemplate.query(
                "SELECT table_name FROM information_schema.tables\n" +
                        "WHERE table_schema IN('public', 'myschema');",
                (rs, rowNum) -> rs.getString("table_name"));

        List<List<String>> lists = List.of(ourTables);
        for (String ourTable : ourTables) {
            if (lists.contains(ourTable)) return true;
        }
        return false;
    }

    @Override
    public void close(){
        try {
            Objects.requireNonNull(this.jdbcTemplate.getDataSource()).getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
