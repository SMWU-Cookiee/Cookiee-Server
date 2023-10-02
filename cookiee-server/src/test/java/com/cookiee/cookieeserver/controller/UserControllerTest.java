package com.cookiee.cookieeserver.controller;

import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserControllerTest {
    @Autowired UserController userController;
    @Autowired UserRepository userRepository;

    @Test
    public void testUserGet(){
        User user1 = new User(1, "스프링1", "스프링 메일1", "이미지1", "설명1");
        userRepository.save(user1);
        User user2 = new User(2, "스프링2", "스프링 메일2", "이미지2", "설명2");
        userRepository.save(user2);

        // 객체를 넣고 id로 찾은 후, 처음과 같은 객체를 찾으면 테스트 통과
        Assertions.assertEquals(user1, userRepository.findById(user1.getUserId()).get());
        Assertions.assertEquals(user2, userRepository.findById(user2.getUserId()).get());
    }
}
