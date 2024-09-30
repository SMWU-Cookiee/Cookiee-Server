package com.cookiee.cookieeserver.login.google;

import com.cookiee.cookieeserver.global.domain.AuthProvider;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();


    public OAuthResponse socialLogin(String socialId) {
/*
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
        System.out.println("userResourceNode = " + userResourceNode);

        String socialId = userResourceNode.has("id") ? userResourceNode.get("id").asText() : null;
        String email = userResourceNode.has("email") ? userResourceNode.get("email").asText() : null;
        System.out.println("id = " + socialId);
        System.out.println("email = " + email);
*/

        User foundUser = userRepository
                .findBySocialLoginTypeAndSocialId(AuthProvider.GOOGLE, socialId)
                .orElse(null);

        if (foundUser == null) {
            log.debug("socialId가 {}인 유저는 존재하지 않음. 신규 회원가입", socialId);
            return OAuthResponse.builder()
                    .socialId(socialId)
                    .email("")
                    .socialType("google")
                    .isNewMember(true)
                    .build();
        }

        else {
            log.debug("socialId가 {}인 유저는 기존 유저입니다.", socialId);
            String appRefreshToken = jwtService.createRefreshToken();
            String appAccessToken = jwtService.createAccessToken(foundUser.getUserId());
            foundUser.setRefreshToken(appRefreshToken);
            userRepository.save(foundUser);

            return OAuthResponse.builder()
                    .socialId(socialId)
                    .email("")
                    .socialType("google")
                    .accessToken(appAccessToken)
                    .refreshToken(appRefreshToken)
                    .isNewMember(false)
                    .build();
        }
    }
    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
