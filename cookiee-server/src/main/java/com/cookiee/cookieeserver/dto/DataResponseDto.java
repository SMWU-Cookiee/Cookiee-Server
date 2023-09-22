package com.cookiee.cookieeserver.dto;

import com.cookiee.cookieeserver.constant.StatusCode;
import lombok.Getter;

/* 통신이 제대로 됐을 때 Response DTO */
@Getter
public class DataResponseDto<T> extends BaseResponseDto {
    private final T result;

    // 생성자 without 메시지
    private DataResponseDto(T result){
        super(true, StatusCode.OK.getCode(), StatusCode.OK.getMessage());
        this.result = result;
    }

    // 생성자 with 특정 메시지
    private DataResponseDto(T result, String message) {
        super(true, StatusCode.OK.getCode(), message);
        this.result = result;
    }

    public static <T> DataResponseDto<T> of(T result) {
        return new DataResponseDto<>(result);
    }

    public static <T> DataResponseDto<T> of(T result, String message) {
        return new DataResponseDto<>(result, message);
    }

    public static <T> DataResponseDto<T> empty() {
        return new DataResponseDto<>(null);
    }
}
