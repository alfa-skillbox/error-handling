package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorId;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorList;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers.ErrorMessages;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers.ExceptionConverter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Slf4j
// За счет этого перебивается ExampleOfApplicationLayerControllerAdvice
@Order(1)
@ControllerAdvice
@ConditionalOnBean(ExceptionConverter.class)
@RequiredArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final ExceptionConverter exConverter;

    @PostConstruct
    public void logConfig() {
        log.info(String.format("%s has bean initialized", this.getClass().getSimpleName()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex, HttpServletRequest request) {
        log.error(ErrorMessages.UNKNOWN_ERROR_MESSAGE, ex);
        return toResponseEntity(exConverter.mapException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        log.error(ErrorMessages.VALIDATION_ERROR_MESSAGE, ex);
        return toResponseEntity(exConverter.mapBindingResult(ex, ex.getBindingResult(), BAD_REQUEST));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        log.error(ErrorMessages.VALIDATION_ERROR_MESSAGE, ex);
        return toResponseEntity(exConverter.mapException(ErrorId.VALIDATION_ERROR, BAD_REQUEST, ex));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        log.error(ErrorMessages.UNKNOWN_ERROR_MESSAGE, ex);
        return toResponseEntity(exConverter.mapException(ex));
    }

    private ResponseEntity<Object> toResponseEntity(ErrorList errorList) {
        return new ResponseEntity<>(
            errorList,
            HttpStatus.valueOf(errorList.getErrors().get(0).getStatus())
        );
    }
}