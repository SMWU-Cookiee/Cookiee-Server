package com.cookiee.cookieeserver.login;

import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.login.dto.request.UserSignupRequestDto;
import com.cookiee.cookieeserver.login.dto.response.AccessTokenResponse;
import com.cookiee.cookieeserver.login.jwt.JwtHeaderUtil;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OAuthController {
    private final JwtService jwtService;
    private final OAuthService oAuthService;

    /**
     * 새로 가입한 사용자가 소셜 로그인 후 회원 정보 입력할 때
     * @return
     */
    @PostMapping(value = "/auth/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<?> signup(UserSignupRequestDto userSignupRequestDto) {
        try{
            OAuthResponse response = oAuthService.signup(userSignupRequestDto);
            log.info("[OAuthController] response: {}", response);
            return DataResponseDto.of(response);
        }
        catch(Exception e){
            log.debug(e.getMessage());
            return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    /**
     * 회원 탈퇴
     * @return
     */
    @DeleteMapping("/auth/signout")
    public BaseResponseDto<?> signout() throws Exception {
        String accessToken = JwtHeaderUtil.getAccessToken();
        Long userId = jwtService.getUserId(accessToken);

        oAuthService.signout(userId);
        return DataResponseDto.of(null, "회원 탈퇴에 성공하였습니다.");
    }

    /**
     * 액세스 토큰 갱신
     * @return
     * @throws Exception
     */
    @PostMapping("/auth/refresh")
    public BaseResponseDto<?> refresh() {
        try {
            AccessTokenResponse response = jwtService.reissueAccessToken();
            return DataResponseDto.of(response);
        }
        catch (Exception e){
            return ErrorResponseDto.of(StatusCode.VALIDATION_ERROR, e.getMessage());
        }
    }

    /**
     * 로그아웃
     */
//    @PutMapping("/logout")
//    public BaseResponseDto<?> logout() {
//
//    }
}
