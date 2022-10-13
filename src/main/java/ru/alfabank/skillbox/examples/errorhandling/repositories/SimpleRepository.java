package ru.alfabank.skillbox.examples.errorhandling.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SimpleRepository {

    Integer create(String value) throws SQLException;

    String read(Integer id) throws SQLException;

    String update(Integer id, String value) throws SQLException;

    String delete(Integer id) throws SQLException;

    @Slf4j
    @Repository
    class SimpleRepositoryImpl implements SimpleRepository {

        private final Map<Integer, String> datasource = new ConcurrentHashMap<>();

        @Override
        public Integer create(String value) throws SQLException {
            throw new SQLException("Create exception");
        }

        @Override
        public String read(Integer id) throws SQLException {
            throw new SQLDataException();
        }

        @Override
        public String update(Integer id, String value) throws SQLException {
            throw new SQLException("Update exception");
        }

        @Override
        public String delete(Integer id) throws SQLException {
            throw new SQLException("Update exception");
        }
    }
}
