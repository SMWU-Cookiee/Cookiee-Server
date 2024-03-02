package com.cookiee.cookieeserver.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserRequestDto {
    private String nickname;
    private String selfDescription;
    private String profileImage;
}
