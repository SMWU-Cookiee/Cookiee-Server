package com.cookiee.cookieeserver.controller;

import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;
//    public BaseResponse2<UserDto> getUser(@PathVariable Long userId) {
//        UsersResponseDto usersResponseDto = UsersResponseDto.from(userRepository.findByUserId(userId)); // 유저 정보
//        List<MyPlantResponseDto> myPlantResponseDtos = myPlantRepository.findAllByUserIdx(userId); // 내 식물 리스트
//
//        return new BaseResponse2<>(new UserDto(usersResponseDto, myPlantResponseDtos));
//    }
    // 유저 프로필 조회
    @GetMapping("/users/{userId}")
    public BaseResponseDto<User> getUser(@PathVariable int userId){
        User user = userRepository.findById(userId).orElseThrow(new Supplier<IllegalArgumentException>(){
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 존재하지 않습니다. id: " + userId);
            }
        });

        return new BaseResponseDto<User>();
    }

    // 유저 프로필 수정 (닉네임, 한줄 소개, 프로필사진)
    @Transactional
    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody User requestUser){
        User newUser = userRepository.findById(userId).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("프로필 수정에 실패하였습니다.");
            }
        });
        newUser.setNickname(requestUser.getNickname());
        newUser.setSelfDescription(requestUser.getSelfDescription());
        newUser.setProfileImage(requestUser.getProfileImage());

        return newUser;
    }

    // 유저 프로필 삭제
    //@DeleteMapping("test/users/{userId}")

    // 임시로 User 추가
    @PostMapping("/users/join")
    public String postTestUser(User user) {
        System.out.println("id: " +user.getUserId());
        System.out.println("nickname: " +user.getNickname());
        System.out.println("email: " +user.getEmail());
        System.out.println("profileImage: " +user.getProfileImage());
        System.out.println("describe: " +user.getSelfDescription());
        System.out.println("created date: " +user.getCreatedDate());

        userRepository.save(user);
        return "회원가입 완료";
    }
}
