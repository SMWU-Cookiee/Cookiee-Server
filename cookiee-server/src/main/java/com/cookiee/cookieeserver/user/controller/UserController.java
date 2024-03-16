package com.cookiee.cookieeserver.user.controller;

import com.cookiee.cookieeserver.global.SuccessCode;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.user.dto.request.UpdateUserRequestDto;
import com.cookiee.cookieeserver.user.dto.response.UserResponseDto;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import com.cookiee.cookieeserver.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // health check에 대한 상태 반환 위해서
    @GetMapping("/healthcheck")
    public BaseResponseDto healthcheck() {
        return BaseResponseDto.ofSuccess(SuccessCode.OK);
    }

    // 유저 프로필 조회
    @GetMapping("/users/{userId}")
    public BaseResponseDto<UserResponseDto> getUser(@PathVariable Long userId){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId(currentUser.getUserId())
                .email(currentUser.getEmail())
                .nickname(currentUser.getNickname())
                .profileImage(currentUser.getProfileImage())
                .selfDescription(currentUser.getSelfDescription())
                .categories(currentUser.getCategories().stream()
                        .map(category -> category.toDto(category))
                        .collect(Collectors.toList()))
                .build();

        return BaseResponseDto.ofSuccess(GET_USER_SUCCESS, userResponseDto);
    }

    // 유저 프로필 수정 (닉네임, 한줄 소개, 프로필사진)
    @PutMapping(value = "/users/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<UserResponseDto> updateUser(@PathVariable Long userId,
                                                        UpdateUserRequestDto requestUser){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        userService.updateUser(currentUser, requestUser);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId(currentUser.getUserId())
                .email(currentUser.getEmail())
                .nickname(currentUser.getNickname())
                .profileImage(currentUser.getProfileImage())
                .selfDescription(currentUser.getSelfDescription())
                .categories(currentUser.getCategories().stream()
                        .map(category -> category.toDto(category))
                        .collect(Collectors.toList()))
                .build();

        return BaseResponseDto.ofSuccess(MODIFY_USER_SUCCESS, userResponseDto);
    }

    // 임시로 User 추가
    // TODO: 삭제하기
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
        return DataResponseDto.of("회원가입에 성공하였습니다.", null);
    }
}
