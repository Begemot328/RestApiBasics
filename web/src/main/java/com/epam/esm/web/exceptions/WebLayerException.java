package com.epam.esm.web.exceptions;

public class WebLayerException extends Exception {
    /**
     * Constructor
     *
     * @param message the message
     */
    public WebLayerException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message
     * @param e the e
     */
    public WebLayerException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor
     *
     * @param e the e
     */
    public WebLayerException(Exception e) {
        super(e);
    }
}
