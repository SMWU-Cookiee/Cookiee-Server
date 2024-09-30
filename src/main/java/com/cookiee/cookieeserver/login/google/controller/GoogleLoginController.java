package com.cookiee.cookieeserver.login.google.controller;

import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.google.GoogleLoginService;
import com.cookiee.cookieeserver.login.google.dto.GoogleInfoResponseDto;
import com.cookiee.cookieeserver.login.google.dto.GoogleRequestDto;
import com.cookiee.cookieeserver.login.google.dto.GoogleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.cookiee.cookieeserver.global.SuccessCode.GOOGLE_LOGIN_SUCCESS;

@RestController
@RequestMapping(value="/api", produces = "application/json")
@Tag(name="Google 소셜 로그인", description="Google 소셜 로그인을 할 수 있습니다.")
public class GoogleLoginController {
    private final GoogleLoginService gooleLoginServcie;

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    @Value("${oauth2.google.client-secret}")
    private String googleClientPW;

    public GoogleLoginController(GoogleLoginService googleLoginService, GoogleLoginService gooleLoginServcie){
        this.gooleLoginServcie = gooleLoginServcie;
    }

    @GetMapping("/google/{socailId}")
    @Operation(summary = "Google 로그인")
    public BaseResponseDto<?> googleLogin(@PathVariable String socailId){
        OAuthResponse oAuthResponse = gooleLoginServcie.socialLogin(socailId);
        return BaseResponseDto.ofSuccess(GOOGLE_LOGIN_SUCCESS, oAuthResponse);
    }

/*    @GetMapping("/code/{registrationId}")
    public String loginGoogle(@RequestParam(value = "code") String authCode){
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequestDto googleOAuthRequestParam = GoogleRequestDto
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPW)
                .code(authCode)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .grantType("authorization_code").build();
        ResponseEntity<GoogleResponseDto> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponseDto.class);
        String jwtToken=resultEntity.getBody().getId_token();
        Map<String, String> map=new HashMap<>();
        map.put("id_token",jwtToken);
        ResponseEntity<GoogleInfoResponseDto> resultEntity2 = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfoResponseDto.class);
        String email=resultEntity2.getBody().getEmail();
        return email;
    }*/
}
