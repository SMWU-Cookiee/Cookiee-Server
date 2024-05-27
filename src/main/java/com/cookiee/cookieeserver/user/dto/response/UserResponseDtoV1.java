package com.cookiee.cookieeserver.user.dto.response;

import com.cookiee.cookieeserver.user.domain.UserV1;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access=PRIVATE)
public class UserResponseDtoV1 {
    private final Long userId;
    private final String deviceId;
    public static UserResponseDtoV1 of(UserV1 userV1){
        return new UserResponseDtoV1(
                userV1.getUserId(),
                userV1.getDeviceId());
    }
}
