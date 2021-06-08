package com.epam.esm.service.constants;

/**
 * Error code suffixes for generating error codes
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class ErrorCodes {

    public static final int CERTIFICATE_BAD_REQUEST = 40001;
    public static final int CERTIFICATE_NOT_FOUND = 40401;
    public static final int CERTIFICATE_VALIDATION_EXCEPTION = 42201;

    public static final int TAG_BAD_REQUEST = 40002;
    public static final int TAG_NOT_FOUND = 40402;
    public static final int TAG_VALIDATION_EXCEPTION = 42202;

    public static final int USER_BAD_REQUEST = 40003;
    public static final int USER_NOT_FOUND = 40403;
    public static final int USER_VALIDATION_EXCEPTION = 42203;

    public static final int ORDER_BAD_REQUEST = 40004;
    public static final int ORDER_NOT_FOUND = 40404;
    public static final int ORDER_VALIDATION_EXCEPTION = 42204;

    private ErrorCodes() {
        // Private constructor to avoid creation.
    }
}
