package com.cookiee.cookieeserver.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // 일반적인 응답
    OK(HttpStatus.OK, "요청에 성공하였습니다."),

    // 인증 & 인가 & 로그인
    APPLE_LOGIN_SUCCESS(HttpStatus.OK, "애플 로그인에 성공하였습니다."),
    SIGNUP_SUCCESS(HttpStatus.OK, "회원가입에 성공하였습니다."),
    SIGNOUT_SUCCESS(HttpStatus.OK, "회원 탈퇴에 성공하였습니다. 빠이염"),
    REISSUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 갱신에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다. 다시 만나요 뿅!"),

    // 유저
    GET_USER_SUCCESS(HttpStatus.OK, "회원 정보 조회 요청에 성공하였습니다."),
    MODIFY_USER_SUCCESS(HttpStatus.OK, "회원 정보 수정에 성공하였습니다."),

    // 카테고리
    CREATE_CATEGORY_SUCCESS(HttpStatus.OK, "카테고리 등록에 성공하였습니다."),
    GET_CATEGORY_SUCCESS(HttpStatus.OK, "카테고리 조회에 성공하였습니다."),
    MODIFY_CATEGORY_SUCCESS(HttpStatus.OK, "카테고리 수정에 성공하였습니다."),
    DELETE_CATEGORY_SUCCESS(HttpStatus.OK, "카테고리 삭제에 성공하였습니다."),

    // 이벤트
    CREATE_EVENT_SUCCESS(HttpStatus.OK, "이벤트 등록에 성공하였습니다."),
    GET_EVENT_SUCCESS(HttpStatus.OK, "이벤트 조회에 성공하였습니다."),
    MODIFY_EVENT_SUCCESS(HttpStatus.OK, "이벤트 수정에 성공하였습니다."),
    DELETE_EVENT_SUCCESS(HttpStatus.OK, "이벤트 삭제에 성공하였습니다."),

    // 썸네일
    CREATE_THUMBNAIL_SUCCESS(HttpStatus.OK, "썸네일 등록에 성공하였습니다."),
    GET_THUMBNAIL_SUCCESS(HttpStatus.OK, "썸네일 조회에 성공하였습니다."),
    MODIFY_THUMBNAIL_SUCCESS(HttpStatus.OK, "썸네일 수정에 성공하였습니다."),
    DELETE_THUMBNAIL_SUCCESS(HttpStatus.OK,"썸네일 삭제에 성공하였습니다."),

    // 컬렉션
    GET_COLLECTION_SUCCESS(HttpStatus.OK, "카테고리 모아보기 조회 요청에 성공하였습니다."),

        ;
    private final HttpStatus httpStatus;
    private final String message;
}
