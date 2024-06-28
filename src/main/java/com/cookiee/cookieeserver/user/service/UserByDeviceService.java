package com.cookiee.cookieeserver.user.service;

import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.dto.response.UserByDeviceResponseDto;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.cookiee.cookieeserver.global.ErrorCode.USER_EXISTS;

@Service
@Transactional
@RequiredArgsConstructor
public class UserByDeviceService {
    private final UserRepository userV1Repository;
    public UserByDeviceResponseDto registerUser(String deviceId) {
        Optional<User> user = userV1Repository.findByDeviceId(deviceId);

        if (user.isPresent()) {
            return UserByDeviceResponseDto.of(userV1Repository.save(user.get()), true);
        }
        else {
            User deviceUser = User.deviceBuilder()
                    .deviceId(deviceId)
                    .build();
            return UserByDeviceResponseDto.of(userV1Repository.save(deviceUser), false);
        }
    }
}
