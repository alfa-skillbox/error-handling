package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception;

/**
 * Resource not found
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
