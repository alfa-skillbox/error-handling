package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The universal representation of an error
 *
 */
@Data
@Accessors(chain = true)
public class Error {

    // http status
    private int status;

    // error code conforming frontend
    private String code;

    // human text message
    private String detail;

    private String title;
    private ErrorMeta meta;
}
