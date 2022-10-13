package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorId {

    public static final ErrorId INTERNAL_ERROR = new ErrorId("internalError", "internalErrorInnerCode");
    public static final ErrorId VALIDATION_ERROR = new ErrorId("validationError", "validationErrorInnerCode");
    public static final ErrorId BUSINESS_ERROR = new ErrorId("businessError", "businessErrorInnerCode");

    private final String innerCode;
    private final String outerCode;

    /**
     * @param innerCode from Repository. example: {@code businessErrorInnerCode}
     * @param outerCode example: {@code businessError}
     */
    public ErrorId(String outerCode, String innerCode) {
        this.innerCode = innerCode;
        this.outerCode = outerCode;
    }

    public ErrorId(String outerCode) {
        this(outerCode, null);
    }
}
