package com.cookiee.cookieeserver.user.controller;

import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.user.dto.response.UserResponseDto;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import com.cookiee.cookieeserver.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    // health check에 대한 상태 반환 위해서
    @GetMapping("/healthcheck")
    public BaseResponseDto healthcheck() {
        return BaseResponseDto.of(true, StatusCode.OK);
    }

    // 유저 프로필 조회
    @GetMapping("/users/{userId}")
    public BaseResponseDto<UserResponseDto> getUser(@PathVariable Long userId){
        User user = userService.findOneById(userId);
        UserResponseDto userResponseDto;

        userResponseDto = UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .selfDescription(user.getSelfDescription())
                .categories(user.getCategories().stream()
                        .map(category -> category.toDto(category))
                        .collect(Collectors.toList()))
                .build();

        return DataResponseDto.of(userResponseDto, "회원 정보 조회 요청에 성공하였습니다.");
    }

    // 유저 프로필 수정 (닉네임, 한줄 소개, 프로필사진)
    @Transactional
    @PutMapping("/users/{userId}")
    public BaseResponseDto<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody User requestUser){
        User user = userService.findOneById(userId);

        UserResponseDto userResponseDto;

        user.setNickname(requestUser.getNickname());
        user.setProfileImage(requestUser.getProfileImage());
        user.setSelfDescription(requestUser.getSelfDescription());

        userResponseDto = UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .selfDescription(user.getSelfDescription())
                .categories(user.getCategories().stream()
                        .map(category -> category.toDto(category))
                        .collect(Collectors.toList()))
                .build();

        return DataResponseDto.of(userResponseDto, "회원 정보를 성공적으로 수정하였습니다.");
    }

    // 유저 프로필 삭제
    //@DeleteMapping("test/users/{userId}")

    // 임시로 User 추가
    @PostMapping("/users/join")
    public DataResponseDto<Object> postTestUser(User user) {
        System.out.println("id: " +user.getUserId());
        System.out.println("nickname: " +user.getNickname());
        System.out.println("email: " +user.getEmail());
        System.out.println("profileImage: " +user.getProfileImage());
        System.out.println("describe: " +user.getSelfDescription());
        System.out.println("created date: " +user.getCreatedDate());

        userRepository.save(user);

        //return DataResponseDto.empty();
        return DataResponseDto.of(null, "회원가입에 성공하였습니다.");
    }
}
