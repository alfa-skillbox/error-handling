package ru.alfabank.skillbox.examples.errorhandling.repositories;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SqlExceptionWrapperException extends RuntimeException {

    public SqlExceptionWrapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
