package com.cookiee.cookieeserver.dto;

import com.cookiee.cookieeserver.constant.StatusCode;
import lombok.*;

@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
//@Builder
@ToString
public class BaseResponseDto<T> {
    private final Boolean isSuccess;
    private final int statusCode;
    private final String message;
    private T result;

    // success -> default response
//    public static <T> BaseResponseDto<T> ofSuccess() {
//        return new BaseResponseDto<>(true, 1000,"요청에 성공하였습니다.", null);
//    }
    public static BaseResponseDto of(Boolean isSuccess, StatusCode code) {
        return new BaseResponseDto(isSuccess, code.getCode(), code.getMessage());
    }

    // success -> response with data
//    public static <T> BaseResponseDto<T> ofSuccess(T data) {
//        return new BaseResponseDto<>(true, 1000, "요청에 성공하였습니다.", data);
//    }

    public static BaseResponseDto of(Boolean isSuccess, StatusCode errorCode, Exception e){
        return new BaseResponseDto(isSuccess, errorCode.getCode(), errorCode.getMessage(e));
    }

    // fail -> default response
//    public static <T> BaseResponseDto<T> ofFail() {
//        return new BaseResponseDto<>(false, 2000, "요청에 실패하였습니다.", null);
//    }

    public static BaseResponseDto of(Boolean isSuccess, StatusCode errorCode, String message) {
        return new BaseResponseDto(isSuccess, errorCode.getCode(), errorCode.getMessage(message));
    }
}
