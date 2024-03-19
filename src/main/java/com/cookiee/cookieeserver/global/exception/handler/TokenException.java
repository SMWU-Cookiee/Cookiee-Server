package com.cookiee.cookieeserver.global.exception.handler;

import com.cookiee.cookieeserver.global.ErrorCode;
import com.cookiee.cookieeserver.global.exception.GeneralException;

public class TokenException extends GeneralException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}