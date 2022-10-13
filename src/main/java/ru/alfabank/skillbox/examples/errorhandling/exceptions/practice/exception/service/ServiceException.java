package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Исключение, произошедшее в процессе работы сервиса, т.е. слоя бизнес-логики.
 * <p>
 * Главное предназначение этого исключения - донести до фронта {@code CompositeException} с заполненными {@code code} и {@code title}.
 * <p>
 * Существует разделение на подтипы, поэтому не рекомендуется использовать непосредственно этот класс.
 * <p>
 * Стандартные объекты исключений {@code message} и {@code cause} используются исключительно для логгирования.
 * <ul>
 * <li>{@code message}, если исключение выбрасывается из бизнес логики нами, но требует какого-то логгирования. Отличие message от title в том, что в нем будет содержаться техническая информация, необходимая для дальнейшего анализа исключения по логам</li>
 * <li>{@code cause}, если оборачиваем ожидаемое исключение, например SocketTimeoutException при http вызове</li>
 * </ul>
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class ServiceException extends RuntimeException {

    private String code;
    private String title;
    private String detail;
    private HttpStatus httpStatus = INTERNAL_SERVER_ERROR;

    ServiceException(String code, String title) {
        this.code = code;
        this.title = title;
    }

    ServiceException(String message) {
        super(message);
    }

    ServiceException(Throwable cause) {
        super(cause);
    }

    ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    ServiceException(String code, String title, Throwable cause) {
        super(cause);
        this.code = code;
        this.title = title;
    }
}
