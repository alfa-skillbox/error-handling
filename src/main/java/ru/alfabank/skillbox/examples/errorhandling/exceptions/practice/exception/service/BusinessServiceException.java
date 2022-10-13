package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception.service;

/**
 * Реализация {@link ServiceException}. Представляет собой ошибку, предполагаемую бизнес логикой.
 */
public class BusinessServiceException extends ServiceException {

    public BusinessServiceException(String code, String title) {
        super(code, title);
    }

    public BusinessServiceException(String message) {
        super(message);
    }

    public BusinessServiceException(Throwable cause) {
        super(cause);
    }

    public BusinessServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessServiceException(String code, String title, Throwable cause) {
        super(code, title, cause);
    }
}
