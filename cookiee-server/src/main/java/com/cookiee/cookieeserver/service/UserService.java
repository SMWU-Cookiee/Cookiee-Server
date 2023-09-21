package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findOneById(int userId) {
        return userRepository.findById(userId);
    }

}
