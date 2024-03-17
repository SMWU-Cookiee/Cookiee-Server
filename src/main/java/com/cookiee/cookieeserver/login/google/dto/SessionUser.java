package com.cookiee.cookieeserver.login.google.dto;

import com.cookiee.cookieeserver.user.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String profileImage;
    private String description;

    public SessionUser(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.description = user.getSelfDescription();
    }
}