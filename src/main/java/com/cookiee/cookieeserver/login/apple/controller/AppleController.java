package com.cookiee.cookieeserver.login.apple.controller;

import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.apple.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.cookiee.cookieeserver.global.Constant.*;
import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    // 로그인 성공 시 설정된 redirect url로 받는 authorization code를 service 단에 넘겨줌
    // 정상적으로 로그인 관련 처리가 끝났다면 관련 데이터를 클라이언트에 보내준다.
    @PostMapping("/auth/login/apple")
    @ResponseBody
    public BaseResponseDto<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                                @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) {
        OAuthResponse response = appleService.login(idToken, authorizationCode);
        return BaseResponseDto.ofSuccess(APPLE_LOGIN_SUCCESS, response);
    }

    @PostMapping("/login/apple/callback")
    @ResponseBody
    public BaseResponseDto<?> callback(String id_token, String code, HttpServletRequest request, HttpServletResponse response) {
        log.debug("여기는 애플 컨트롤러");
        log.debug(String.valueOf(request.getParameter("id_token")));
        log.debug(String.valueOf(request.getParameter("code")));
        log.debug(id_token);
        log.debug(code);
        //log.info(String.valueOf(response));
//        request.getParameter("id_token"), request.getParameter("code")
        OAuthResponse authResponse = appleService.login(id_token, code);
        return BaseResponseDto.ofSuccess(APPLE_LOGIN_SUCCESS, authResponse);
    }
}
