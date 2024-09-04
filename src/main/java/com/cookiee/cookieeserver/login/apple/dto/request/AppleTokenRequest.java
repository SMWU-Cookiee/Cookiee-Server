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
}
