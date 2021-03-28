package com.epam.esm.web.exceptions;

import com.epam.esm.model.exceptions.BaseException;

public class WebLayerException extends BaseException {
    public WebLayerException(Exception e) {
        super(e);
    }
}
