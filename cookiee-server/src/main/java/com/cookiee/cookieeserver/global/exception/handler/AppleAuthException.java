package com.cookiee.cookieeserver.global.exception.handler;

import com.cookiee.cookieeserver.global.ErrorCode;
import com.cookiee.cookieeserver.global.exception.GeneralException;

public class AppleAuthException extends GeneralException {

    public AppleAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
