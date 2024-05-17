package com.cookiee.cookieeserver.login.google.controller;

import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.google.GoogleLoginService;
import com.cookiee.cookieeserver.login.google.dto.GoogleInfoResponseDto;
import com.cookiee.cookieeserver.login.google.dto.GoogleRequestDto;
import com.cookiee.cookieeserver.login.google.dto.GoogleResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.cookiee.cookieeserver.global.SuccessCode.GOOGLE_LOGIN_SUCCESS;

@RestController
@RequestMapping(value="/login/oauth2", produces = "application/json")
public class GoogleLoginController {
    private final GoogleLoginService gooleLoginServcie;

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    @Value("${oauth2.google.client-secret}")
    private String googleClientPW;

    public GoogleLoginController(GoogleLoginService googleLoginService, GoogleLoginService gooleLoginServcie){
        this.gooleLoginServcie = gooleLoginServcie;
    }

    @PostMapping("/google")
    public String googleLoginUrl(){
        String url = "https://accounts.google.com/o/oauth2/auth?client_id="+googleClientId+"&redirect_uri=http://localhost:8080/login/oauth2/code/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email";
        //String url = "https://accounts.google.com/o/oauth2/auth?client_id="+googleClientId+"&redirect_uri=https://cookiee.site/login/oauth2/code/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email";
        return url;
    }

    @GetMapping("/code/{registrationId}")
    public BaseResponseDto<?> googleLogin(@RequestParam String code, @PathVariable String registrationId){
        OAuthResponse oAuthResponse = gooleLoginServcie.socialLogin(code, registrationId);
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
