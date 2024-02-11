package com.cookiee.cookieeserver.login.apple.controller;

import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.apple.dto.response.AppleLoginResponse;
import com.cookiee.cookieeserver.login.apple.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.cookiee.cookieeserver.global.Constant.*;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    // 로그인 성공 시 설정된 redirect url로 받는 authorization code를 service 단에 넘겨줌
    // 정상적으로 로그인 관련 처리가 끝났다면 관련 데이터를 클라이언트에 보내준다.
    @PostMapping("/auth/login/apple")
    @ResponseBody
    public BaseResponseDto<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                                @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) {
        try{
            OAuthResponse response = appleService.login(idToken, authorizationCode);
            return DataResponseDto.of(response, "애플 로그인에 성공하였습니다.");
        }
        // 애플 회원가입 또는 로그인 실패
        catch(Exception e){
            return ErrorResponseDto.of(StatusCode.VALIDATION_ERROR, e.getMessage());
        }
    }

//    @RequestMapping("/login/apple/callback")
//    @ResponseBody
//    public AppleLoginResponse callback(HttpServletRequest request, HttpServletResponse response) {
//        return AppleLoginResponse.builder()
//                .code(request.getParameter("code"))
//                .idToken(request.getParameter("id_token"))
//                .build();
//    }
}
