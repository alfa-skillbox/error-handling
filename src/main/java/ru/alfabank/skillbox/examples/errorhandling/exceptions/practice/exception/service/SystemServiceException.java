package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception.service;

/**
 * Реализация {@link ServiceException}. Представляет собой системное исключение, например недоступность ресурса или
 * некорректный набор данных.
 */
public class SystemServiceException extends ServiceException {

    public SystemServiceException(String code, String title) {
        super(code, title);
    }

    public SystemServiceException(String message) {
        super(message);
    }

    public SystemServiceException(Throwable cause) {
        super(cause);
    }

    public SystemServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemServiceException(String code, String title, Throwable cause) {
        super(code, title, cause);
    }
}
