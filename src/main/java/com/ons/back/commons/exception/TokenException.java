package com.ons.back.commons.exception;

import com.ons.back.commons.exception.payload.ErrorStatus;

public class TokenException extends ApplicationException {

    public TokenException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}