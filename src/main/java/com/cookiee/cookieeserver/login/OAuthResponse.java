package com.cookiee.cookieeserver.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthResponse {
    private String socialId;  // 소셜 아이디
    private Long userId;  // 유저 아이디
    private String email;  // 회원 이메일
    private String socialType;  // 소셜 로그인 타입(구글 혹은 애플)
    private String accessToken;  // 액세스 토큰
    private String refreshToken;  // 리프레쉬 토큰
    private Boolean isNewMember;  // 새로운 유저인지 아닌지
}
