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
    IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "IO 예외입니다."),

    // 인증 & 인가
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    //INVALID_TOKEN_SIGNATURE(BAD_REQUEST, "LOGIN4002", "잘못된 토큰 서명입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),  // bearer
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 구성의 토큰입니다."),
    //NULL_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 리프레시 토큰입니다."),
    NULL_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 빈 값입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NULL_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "Authorization Header가 빈 값입니다."),

    // APPLE
    NULL_AUTHENTICATION_CODE(HttpStatus.BAD_REQUEST, "Apple Authentication Code가 빈 값입니다."),
    FAILED_TO_GET_APPLE_TOKEN(HttpStatus.BAD_REQUEST, "Apple에서 사용자 코드를 가져오는데 실패했습니다."),
    FAILED_TO_GET_APPLE_PRIVATE_KEY(HttpStatus.BAD_REQUEST, "Apple 프라이빗 키를 가져오는데 실패했습니다."),
    FAILED_TO_GET_APPLE_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "Apple 공개키를 가져오는데 실패했습니다."),
    INVALID_APPLE_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "Apple 공개키를 가져올 수 없습니다."),
    INVALID_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "토큰을 가져올 수 없습니다."),
    APPLE_SIGNOUT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Apple 탈퇴 실패"),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다"),
    USER_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    TOKEN_AND_USER_NOT_CORRESPONDS(HttpStatus.BAD_REQUEST, "토큰 정보와 유저 정보가 일치하지 않습니다"),

    // 카테고리
    CATEGORY_NAME_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 이름입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다"),

    // 이벤트
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이벤트 아이디입니다."),

    // 컬렉션

    // 사진
    IMAGE_IS_NULL(HttpStatus.BAD_REQUEST, "사진이 없습니다. 사진을 넣어주세요."),
    S3_UPLOAD_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "S3 업로드에 실패하였습니다."),

    THUMBNAIL_IS_NULL(HttpStatus.BAD_REQUEST, "해당 날짜에 썸네일이 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}