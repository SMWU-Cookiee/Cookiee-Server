package com.cookiee.cookieeserver.login.dto.request;

import com.cookiee.cookieeserver.global.domain.AuthProvider;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserSignupRequestDto {
    private String email;
    private String name;
    private String nickname;
    private String selfDescription;
    private String socialId;
    private String socialLoginType;
    //private String socialRefreshToken;
    //private MultipartFile profileImage;
}