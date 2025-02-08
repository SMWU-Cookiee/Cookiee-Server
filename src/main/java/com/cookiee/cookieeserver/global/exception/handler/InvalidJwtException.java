package com.cookiee.cookieeserver.global.exception.handler;

import com.cookiee.cookieeserver.global.ErrorCode;
import com.cookiee.cookieeserver.global.exception.GeneralException;

public class InvalidJwtException extends GeneralException {

    public InvalidJwtException(ErrorCode code) {
        super(code);
    }
}
