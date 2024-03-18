package com.cookiee.cookieeserver.user.service;

import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findOneById(Long userId) {
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty())
            throw new GeneralException(USER_NOT_FOUND);

        return user.get();
    }

}
