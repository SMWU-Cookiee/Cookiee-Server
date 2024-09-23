package com.cookiee.cookieeserver.login.apple.controller;

import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.apple.service.AppleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api")
@Tag(name="Apple 소셜 로그인", description="Apple 소셜 로그인을 할 수 있습니다.")
public class AppleController {
    private final AppleService appleService;

    @PostMapping("/auth/login/apple")
    @ResponseBody
    @Operation(summary = "Apple 로그인")
    public BaseResponseDto<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                                @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) {
        OAuthResponse response = appleService.login(idToken, authorizationCode);
        return BaseResponseDto.ofSuccess(APPLE_LOGIN_SUCCESS, response);
    }

    @PostMapping("/login/apple/callback")
    @ResponseBody
    @Operation(summary = "Apple 콜백")
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
