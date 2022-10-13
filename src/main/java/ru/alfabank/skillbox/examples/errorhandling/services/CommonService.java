package ru.alfabank.skillbox.examples.errorhandling.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.alfabank.skillbox.examples.errorhandling.repositories.CommonDatabaseException;
import ru.alfabank.skillbox.examples.errorhandling.repositories.SimpleRepository;
import ru.alfabank.skillbox.examples.errorhandling.repositories.SqlDataExceptionWrapperException;
import ru.alfabank.skillbox.examples.errorhandling.repositories.SqlExceptionWrapperException;

import java.sql.SQLException;

public interface CommonService {

    Integer create(String value);

    String read(String username, Integer id);

    String update(String username, Integer id, String value);

    String delete(Authentication authentication, Integer id);

    @Slf4j
    @Service
    @RequiredArgsConstructor
    class DefaultCommonServiceImpl implements CommonService {

        private final SimpleRepository repository;

        @Override
        public Integer create(String value) {
            try {
                return repository.create(value);
            } catch (SQLException se) {
                throw new CommonDatabaseException(se.getMessage(), se.getCause());
            }
        }

        @Override
        public String read(String username, Integer id) {
            try {
                return repository.read(id);
            } catch (SQLException se) {
                // здесь есть вся необходимая информация для логирования, поэтому логируем тут
                log.error("User {} can't read id {}", username, id);
                throw new SqlDataExceptionWrapperException(se.getMessage(), se.getCause());
            }
        }

        @Override
        public String update(String username, Integer id, String value) {
            try {
                return repository.update(id, value);
            } catch (SQLException e) {
                log.error("User {} did not update id {} with value {} because of exception", username, id, value);
                throw new SqlExceptionWrapperException(e.getMessage(), e.getCause());
            }
        }

        @Override
        public String delete(Authentication authentication, Integer id) {
            try {
                return repository.delete(id);
            } catch (SQLException e) {
                // если ловим и не прокидываем, то нельзя оставлять без лога
                String msg = String.format("Exception during deletion of id: %s by user %s", id, authentication.getName());
                log.error(msg, e.getMessage());
                return null;
            }
        }
    }
}
