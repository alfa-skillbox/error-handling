package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Additional error info
 *
 */
@Data
@Accessors(chain = true)
public class ErrorMeta {

    private String jExceptionMsg;

    public static ErrorMeta fromException(Exception exception) {
        return new ErrorMeta().setJExceptionMsg(exception.toString());
    }

    public static ErrorMeta ofJExceptionMsgFormat(String format, Object... formatArgs) {
        if (formatArgs.length > 0) {
            return new ErrorMeta().setJExceptionMsg(String.format(format, formatArgs));
        } else {
            return new ErrorMeta().setJExceptionMsg(format);
        }
    }
}
