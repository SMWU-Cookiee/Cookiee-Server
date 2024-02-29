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
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효한 토큰이 아닙니다."),
    //INVALID_TOKEN_SIGNATURE(BAD_REQUEST, "LOGIN4002", "잘못된 토큰 서명입니다."),
    UNSUPPORTED_TOKEN_FORM(HttpStatus.BAD_REQUEST, "지원하지 않는 형식의 토큰입니다."),  // bearer
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 구성의 토큰입니다."),
    //NULL_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 리프레시 토큰입니다."),
    NULL_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰이 빈 값입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    NULL_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "Authorization Header가 빈 값입니다."),

    // APPLE
    INVALID_APPLE_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "Apple 공개키를 가져올 수 없습니다."),
    INVALID_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "토큰을 가져올 수 없습니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다"),

    // 카테고리
    CATEGORY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 이름 혹은 색상입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다"),

    // 이벤트

    // 컬렉션

    ;


    private final HttpStatus httpStatus;
    private final String message;
}
