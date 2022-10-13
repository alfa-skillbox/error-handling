package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.Error;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorId;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorList;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorMeta;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception.ResourceNotFoundException;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception.UserNotFoundException;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.exception.service.ServiceException;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public class ExceptionConverter {

    private final ErrorBuilder errorBuilder;

    @SuppressWarnings({"CyclomaticComplexity", "ReturnCount", "BooleanExpressionComplexity"})
    public ErrorList mapException(Exception ex) {
        if (ex instanceof ValidationException) {
            return mapConstraintValidationException((ValidationException) ex);
        } else if (ex instanceof IllegalArgumentException || ex instanceof HttpMessageNotReadableException
                || ex instanceof TypeMismatchException || ex instanceof BindException
                || ex instanceof MissingServletRequestParameterException || ex instanceof MultipartException
                || ex instanceof MissingServletRequestPartException
        ) {
            return mapException(ErrorId.VALIDATION_ERROR, BAD_REQUEST, ex);
        } else if (ex instanceof AccessDeniedException) {
            return mapException(ErrorId.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, ex);
        } else if (ex instanceof UserNotFoundException) {
            return mapException(ErrorId.INTERNAL_ERROR, HttpStatus.NOT_FOUND, ex);
        } else if (ex instanceof ServiceException) {
            return mapServiceException((ServiceException) ex);
        } else if (ex instanceof ResourceNotFoundException) {
            return mapException(ErrorId.INTERNAL_ERROR, HttpStatus.NOT_FOUND, ex);
        } else if (ex instanceof WebExchangeBindException) {
            return mapWebExchangeBindException((WebExchangeBindException) ex);
        } else {
            return mapException(ErrorId.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    public ErrorList mapException(ErrorId errorId, HttpStatus status, Exception ex) {
        return new ErrorList(
                errorBuilder.forErrorId(errorId)
                        .meta(ErrorMeta.fromException(ex))
                        .status(status)
                        .build()
        );
    }

    public ErrorList mapConstraintValidationException(ValidationException ex) {
        return new ErrorList(errorBuilder.forErrorId(ErrorId.VALIDATION_ERROR)
                .meta(ErrorMeta.fromException(ex))
                .detail(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build());
    }

    public ErrorList mapServiceException(ServiceException ex) {
        ErrorId errorId = isEmpty(ex.getCode()) ? ErrorId.BUSINESS_ERROR : new ErrorId(ex.getCode());

        return new ErrorList(errorBuilder.forErrorId(errorId)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .status(ex.getHttpStatus())
                .detail(ex.getDetail())
                .title(ex.getTitle())
                .build());
    }

    public ErrorList mapBindingResult(Exception cause, BindingResult bindingResult, HttpStatus httpStatus) {
        return new ErrorList(Optional.ofNullable(bindingResult)
                .map(Errors::getAllErrors)
                .filter(errors -> !errors.isEmpty())

                // map all validation errors to list
                .map(errors -> errors.stream()
                        .map(err -> errorBuilder.forErrorId(ErrorId.VALIDATION_ERROR)
                                .status(httpStatus)
                                .detail(err.getDefaultMessage())
                                .meta(ErrorMeta.ofJExceptionMsgFormat("%s: %s", err.getClass(),
                                        Arrays.toString(err.getArguments())))
                                .build())
                        .collect(toList()))

                // fallback to generic response if no errors list exist
                .orElse(singletonList(errorBuilder.forErrorId(ErrorId.VALIDATION_ERROR)
                        .status(httpStatus)
                        .meta(ErrorMeta.fromException(cause))
                        .build())));
    }

    @SuppressWarnings("MultipleStringLiterals")
    private ErrorList mapWebExchangeBindException(WebExchangeBindException ex) {
        ErrorList errorList = new ErrorList();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldDefaultMessage = fieldError.getDefaultMessage();
            String methodName = ex.getMethodParameter().getMethod().getName();
            String fieldName = fieldError.getField();
            String detailMessage = methodName + "." + fieldName + ": " + fieldDefaultMessage;
            String exceptionName = ex.getClass().getName();
            String errorMessage = exceptionName + ": " + detailMessage;

            Error error = errorBuilder.forErrorId(ErrorId.VALIDATION_ERROR)
                    .meta(new ErrorMeta().setJExceptionMsg(errorMessage))
                    .detail(detailMessage)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            errorList.add(error);
        });
        return errorList;
    }
}