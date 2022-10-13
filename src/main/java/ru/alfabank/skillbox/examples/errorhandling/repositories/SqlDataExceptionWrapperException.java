package ru.alfabank.skillbox.examples.errorhandling.repositories;

public class SqlDataExceptionWrapperException extends RuntimeException {

    public SqlDataExceptionWrapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
