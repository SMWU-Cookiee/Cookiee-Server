package com.cookiee.cookieeserver.global.dto;

import com.cookiee.cookieeserver.global.StatusCode;

/* 통신 실패했을 때 Response Dto */
public class ErrorResponseDto extends BaseResponseDto {

    // 생성자 without 메세지
    private ErrorResponseDto(StatusCode errorCode) {
        super(false, errorCode.getCode(), errorCode.getMessage());
    }

    // 생성자 with exception 상황
    private ErrorResponseDto(StatusCode errorCode, Exception e) {
        super(false, errorCode.getCode(), errorCode.getMessage(e));
    }

    // 생성자 with 메세지
    private ErrorResponseDto(StatusCode errorCode, String message) {
        super(false, errorCode.getCode(), errorCode.getMessage(message));
    }


    public static ErrorResponseDto of(StatusCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto of(StatusCode errorCode, Exception e) {
        return new ErrorResponseDto(errorCode, e);
    }

    public static ErrorResponseDto of(StatusCode errorCode, String message) {
        return new ErrorResponseDto(errorCode, message);
    }
}
