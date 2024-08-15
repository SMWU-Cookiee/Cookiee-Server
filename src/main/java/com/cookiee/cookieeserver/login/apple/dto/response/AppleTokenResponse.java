package com.cookiee.cookieeserver.login.apple.dto.response;

import lombok.Setter;

// 애플에 AppleTokenRequest를 통해 토큰을 발급받으면 받는 응답
@Setter
public class AppleTokenResponse {
// 일반적으로 응답받은 access_token을 session유지에 사용하겠지만 우리가 필요한건 access_token이 아닌 refresh_token이다.
// 무슨 생각인지는 모르겠지만 결국 우리는 APP측에서 refresh_token을 가지고 session을 유지해야한다.
// 아래가 응답받는 내용임
//    {
//        "access_token": "....",
//        "token_type": "Bearer",
//        "expires_in": 3600,
//        "refresh_token": "....",
//        "id_token": "...."
//    }

// 이제 여기서 refresh token을 얻었다면 로그인 또는 인증이 필요할 때마다
// refresh token을 통해 access token을 재발급 받아보는 형식으로 refresh token이 유효한지 검증해주면 된다.
    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;
    private String error;

    public String getAccessToken() {
        return access_token;
    }

    public String getExpiresIn() {
        return expires_in;
    }

    public String getIdToken() {
        return id_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getTokenType() {
        return token_type;
    }
}