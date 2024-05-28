package com.cookiee.cookieeserver.user.dto.response;

import com.cookiee.cookieeserver.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access=PRIVATE)
public class UserByDeviceResponseDto {
    private final Long userId;
    private final String deviceId;
    public static UserByDeviceResponseDto of(User user){
        return new UserByDeviceResponseDto(
                user.getUserId(),
                user.getDeviceId());
    }
}
