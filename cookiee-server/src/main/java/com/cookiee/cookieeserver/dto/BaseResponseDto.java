package com.cookiee.cookieeserver.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Builder
public class BaseResponseDto<T> {
    private Boolean isSuccess;
    private int statusCode;
    private String message;
    private T result;

    // success -> default response
    public static <T> BaseResponseDto<T> ofSuccess() {
        return new BaseResponseDto<>(true, 1000,"요청에 성공하였습니다.", null);
    }

    // success -> response with data
    public static <T> BaseResponseDto<T> ofSuccess(T data) {
        return new BaseResponseDto<>(true, 1000, "요청에 성공하였습니다.", data);
    }

    // fail -> default response
    public static <T> BaseResponseDto<T> ofFail() {
        return new BaseResponseDto<>(false, 2000, "요청에 실패하였습니다.", null);
    }
}
