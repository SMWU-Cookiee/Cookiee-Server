package com.cookiee.cookieeserver.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러, 문의 바랍니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 인증 & 인가
    // INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID INPUT VALUE"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    NULL_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "Authorization Header가 빈 값입니다."),
    INVALID_HEADER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "해당 refresh token은 만료되었습니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다"),

    // 카테고리
    CATEGORY_EXSITS(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 이름 혹은 색상입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다"),

    // 이벤트

    // 컬렉션

    ;


    private final HttpStatus httpStatus;
    private final String message;
}
