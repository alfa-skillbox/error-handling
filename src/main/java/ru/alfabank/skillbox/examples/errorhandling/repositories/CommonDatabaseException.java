package ru.alfabank.skillbox.examples.errorhandling.repositories;

public class CommonDatabaseException extends RuntimeException {

    public CommonDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
