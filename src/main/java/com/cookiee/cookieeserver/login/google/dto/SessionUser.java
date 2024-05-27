package com.cookiee.cookieeserver.login.google.dto;

import com.cookiee.cookieeserver.user.domain.UserV2;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String profileImage;
    private String description;

    public SessionUser(UserV2 userV2) {
        this.nickname = userV2.getNickname();
        this.email = userV2.getEmail();
        this.profileImage = userV2.getProfileImage();
        this.description = userV2.getSelfDescription();
    }
}