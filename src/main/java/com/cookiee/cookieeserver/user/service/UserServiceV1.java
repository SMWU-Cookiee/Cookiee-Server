package com.cookiee.cookieeserver.user.service;

import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.UserV1;
import com.cookiee.cookieeserver.user.dto.response.UserResponseDtoV1;
import com.cookiee.cookieeserver.user.repository.UserRepositoryV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.cookiee.cookieeserver.global.ErrorCode.USER_EXISTS;
import static com.cookiee.cookieeserver.user.domain.UserV1.registerNewUser;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceV1 {
    private final UserRepositoryV1 userV1Repository;
    public UserResponseDtoV1 registerUser(String deviceId) {
        if (userV1Repository.findByDeviceId(deviceId).isPresent())
            throw new GeneralException(USER_EXISTS);
        return UserResponseDtoV1.of(userV1Repository.save(registerNewUser(deviceId)));
    }
}
