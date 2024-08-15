package com.cookiee.cookieeserver.login.apple.dto.request;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class AppleTokenRequest {
        private String code;
        private String client_id;
        private String client_secret;
        private String grant_type;
        private String refresh_token;

//        public static AppleTokenRequest of(String code, String clientId, String clientSecret, String grantType, String refreshToken) {
//            AppleTokenRequest request = new AppleTokenRequest();
//            request.setCode(code);
//            request.setClient_id(clientId);
//            request.setClient_secret(clientSecret);
//            request.setGrant_type(grantType);
//            request.setRefresh_token(refreshToken);
//            return request;
//        }
}
