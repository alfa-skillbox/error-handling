package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers;

import lombok.experimental.UtilityClass;

/**
 * Messages that will be written in log on exceptions.
 */
@UtilityClass
public class ErrorMessages {

    public static final String VALIDATION_ERROR_MESSAGE = "Some of input parameters are incorrect";
    public static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer wasn't found for given id";

    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error has occurred";

    public static final String ACCESS_DENIED_MESSAGE = "User doesn't have enough permissions for operation";
    public static final String USER_NOT_FOUND_MESSAGE = "User is not found. Most likely given profileId is not presented in EQ";
    public static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource is not found";
}
