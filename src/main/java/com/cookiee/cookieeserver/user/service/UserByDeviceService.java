package com.cookiee.cookieeserver.user.service;

import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.dto.response.UserByDeviceResponseDto;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cookiee.cookieeserver.global.ErrorCode.USER_EXISTS;

@Service
@Transactional
@RequiredArgsConstructor
public class UserByDeviceService {
    private final UserRepository userV1Repository;
    public UserByDeviceResponseDto registerUser(String deviceId) {
        if (userV1Repository.findByDeviceId(deviceId).isPresent())
            throw new GeneralException(USER_EXISTS);
        User deviceUser = User.deviceBuilder()
                .deviceId(deviceId)
                .build();
        return UserByDeviceResponseDto.of(userV1Repository.save(deviceUser));
    }
}
