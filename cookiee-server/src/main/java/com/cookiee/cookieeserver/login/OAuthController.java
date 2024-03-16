package com.cookiee.cookieeserver.login;

import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.login.dto.request.UserSignupRequestDto;
import com.cookiee.cookieeserver.login.dto.response.AccessTokenResponse;
import com.cookiee.cookieeserver.login.jwt.JwtHeaderUtil;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
//@RequestMapping("/auth")
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
        OAuthResponse response = oAuthService.signup(userSignupRequestDto);
        return BaseResponseDto.ofSuccess(SIGNUP_SUCCESS, response);
    }

    /**
     * 회원 탈퇴
     * @return
     */
    @DeleteMapping("/auth/signout")
    public BaseResponseDto<?> signout(){
        String accessToken = JwtHeaderUtil.getAccessToken();
        Long userId = jwtService.getUserId(accessToken);

        oAuthService.signout(userId);
        return BaseResponseDto.ofSuccess(SIGNOUT_SUCCESS);
    }

    /**
     * 액세스 토큰 갱신
     * @return
     * @throws Exception
     */
    @PostMapping("/auth/refresh")
    public BaseResponseDto<?> refresh() {
//        try {
            AccessTokenResponse response = jwtService.reissueAccessToken();
            return BaseResponseDto.ofSuccess(REISSUE_TOKEN_SUCCESS, response);
//        }
//        catch (Exception e){
//            return ErrorResponseDto.of(StatusCode.VALIDATION_ERROR, e.getMessage());
//        }
    }

    /**
     * 로그아웃
     */
//    @PutMapping("/logout")
//    public BaseResponseDto<?> logout() {
//
//    }
}
