package com.cookiee.cookieeserver.user.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequestDto {
    private String nickname;
    private String selfDescription;
    private MultipartFile profileImage;
}