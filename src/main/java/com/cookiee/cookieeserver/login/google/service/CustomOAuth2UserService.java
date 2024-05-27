package com.cookiee.cookieeserver.login.google.service;

import com.cookiee.cookieeserver.login.google.dto.OAuthAttributes;
import com.cookiee.cookieeserver.login.google.dto.SessionUser;
import com.cookiee.cookieeserver.user.domain.UserV2;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        UserV2 userV2 = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(userV2));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(
                        userV2.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private UserV2 saveOrUpdate(OAuthAttributes attributes) {
        UserV2 userV2 = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getNickname(), attributes.getProfileImage(), attributes.getDescription()))
                .orElse(attributes.toEntity());

        return userRepository.save(userV2);
    }
}