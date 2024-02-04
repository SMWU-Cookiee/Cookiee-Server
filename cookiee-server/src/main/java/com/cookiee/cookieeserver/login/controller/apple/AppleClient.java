package com.cookiee.cookieeserver.login.controller.apple;

import com.cookiee.cookieeserver.config.auth.FeignClientConfig;
import com.cookiee.cookieeserver.login.dto.response.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth", configuration = FeignClientConfig.class)
public interface AppleClient {
    // public key get 요청
    @GetMapping(value = "/keys")
    ApplePublicKeyResponse getAppleAuthPublicKey();

    // 토큰 등 발급 요청
//    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded", headers = {
//
//    })
//    AppleTokenResponse getToken(AppleTokenRequest request);
}
