package com.cookiee.cookieeserver.login.apple.controller;

import com.cookiee.cookieeserver.login.apple.dto.request.AppleTokenRequest;
import com.cookiee.cookieeserver.login.apple.dto.response.AppleTokenResponse;
import com.cookiee.cookieeserver.login.config.FeignClientConfig;
import com.cookiee.cookieeserver.login.apple.dto.response.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth", configuration = FeignClientConfig.class)
public interface AppleClient {
    // public key get 요청
    @GetMapping(value = "/keys")
    ApplePublicKeyResponse getAppleAuthPublicKey();

    // 토큰 등 발급 요청
    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleTokenResponse getAppleToken(@RequestBody AppleTokenRequest request);
//    AppleTokenResponse getToken(AppleTokenRequest request);
}
