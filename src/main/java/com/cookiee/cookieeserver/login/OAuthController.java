package com.cookiee.cookieeserver.login;

import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.login.dto.request.UserSignupRequestDto;
import com.cookiee.cookieeserver.login.dto.response.AccessTokenResponse;
import com.cookiee.cookieeserver.login.jwt.JwtHeaderUtil;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name="유저 가입/탈퇴, 로그아웃, 토큰갱신", description="유저의 회원가입/탈퇴와 토큰 갱신, 로그아웃을 할 수 있습니다.")
public class OAuthController {
    private final JwtService jwtService;
    private final OAuthService oAuthService;

    /**
     * 새로 가입한 사용자가 소셜 로그인 후 회원 정보 입력할 때
     * @return
     */
    @PostMapping(value = "/signup")
    @Operation(summary = "소셜 로그인 후 회원가입")
    public BaseResponseDto<?> signup(@RequestParam(value = "image") MultipartFile image, UserSignupRequestDto userSignupRequestDto) {
        OAuthResponse response = oAuthService.signup(image, userSignupRequestDto);
        return BaseResponseDto.ofSuccess(SIGNUP_SUCCESS, response);
    }

    /**
     * 회원 탈퇴
     * @return
     */
    @DeleteMapping("/signout")
    @Operation(summary = "회원탈퇴")
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
    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 갱신")
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
     * @return
     */
    @PutMapping("/logout")
    @Operation(summary = "로그아웃")
    public BaseResponseDto<?> logout() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        Long userId = jwtService.getUserId(accessToken);
        oAuthService.logout(userId);
        return BaseResponseDto.ofSuccess(LOGOUT_SUCCESS);
    }
}