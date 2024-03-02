package com.cookiee.cookieeserver.global.dto;

import com.cookiee.cookieeserver.global.ErrorCode;
import com.cookiee.cookieeserver.global.SuccessCode;
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

    public static BaseResponseDto<?> ofSuccess(SuccessCode code) {
        return DataResponseDto.of(code.getMessage());
    }

    public static <T> DataResponseDto<T> ofSuccess(SuccessCode code, T result) {
        return DataResponseDto.of(code.getMessage(), result);
    }

    public static BaseResponseDto ofFailure(ErrorCode code) {
        return ErrorResponseDto.of(code.getHttpStatus(), code.getMessage());
    }

    // success -> response with data
//    public static <T> BaseResponseDto<T> ofSuccess(T data) {
//        return new BaseResponseDto<>(true, 1000, "요청에 성공하였습니다.", data);
//    }

//    public static BaseResponseDto of(Boolean isSuccess, StatusCode errorCode, Exception e){
//        return new BaseResponseDto(isSuccess, errorCode.getCode(), errorCode.getMessage(e));
//    }

    // fail -> default response
//    public static <T> BaseResponseDto<T> ofFail() {
//        return new BaseResponseDto<>(false, 2000, "요청에 실패하였습니다.", null);
//    }

//    public static BaseResponseDto of(Boolean isSuccess, StatusCode errorCode, String message) {
//        return new BaseResponseDto(isSuccess, errorCode.getCode(), errorCode.getMessage(message));
//    }
}
