package com.cookiee.cookieeserver.login.google.controller;

import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.google.GoogleLoginService;
import org.springframework.web.bind.annotation.*;

import static com.cookiee.cookieeserver.global.SuccessCode.GOOGLE_LOGIN_SUCCESS;

@RestController
@RequestMapping(value="/login/oauth2", produces = "application/json")
public class GoogleLoginController {
    private final GoogleLoginService gooleLoginServcie;

    public GoogleLoginController(GoogleLoginService googleLoginService, GoogleLoginService gooleLoginServcie){
        this.gooleLoginServcie = gooleLoginServcie;
    }

    @GetMapping("/code/{registrationId}")
    public BaseResponseDto<?> googleLogin(@RequestParam String code, @PathVariable String registrationId){
        OAuthResponse oAuthResponse = gooleLoginServcie.socialLogin(code, registrationId);
        return BaseResponseDto.ofSuccess(GOOGLE_LOGIN_SUCCESS, oAuthResponse);
    }
}
