package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
