package com.cookiee.cookieeserver.login.controller.apple;

import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.login.service.apple.AppleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.cookiee.cookieeserver.global.Constant.*;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    // 로그인 성공 시 설정된 redirect url로 받는 authorization code를 service 단에 넘겨줌
    // 정상적으로 로그인 관련 처리가 끝났다면 관련 데이터를 클라이언트에 보내준다.
    @PostMapping("/login/apple")
    public BaseResponseDto<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                                @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) {
        // 애플 회원가입 또는 로그인 실패
        if (appleService.login(idToken, authorizationCode) == null) {
            return ErrorResponseDto.of(StatusCode.UNAUTHORIZED, "애플 로그인 실패");
        }

        return DataResponseDto.of(null, "애플 로그인에 성공하였습니다.");
    }
}
