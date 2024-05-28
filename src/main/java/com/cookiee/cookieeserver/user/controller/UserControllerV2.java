package com.cookiee.cookieeserver.user.controller;

import com.cookiee.cookieeserver.global.SuccessCode;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.UserV2;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.user.dto.request.UpdateUserRequestDto;
import com.cookiee.cookieeserver.user.dto.response.UserResponseDtoV2;
import com.cookiee.cookieeserver.user.repository.UserRepositoryV2;
import com.cookiee.cookieeserver.user.service.UserServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class UserControllerV2 {
    private final UserServiceV2 userServiceV2;
    private final UserRepositoryV2 userRepositoryV2;
    private final JwtService jwtService;

    // health check에 대한 상태 반환 위해서
    @GetMapping("/healthcheck")
    public BaseResponseDto healthcheck() {
        return BaseResponseDto.ofSuccess(SuccessCode.OK);
    }

    // 유저 프로필 조회
    @GetMapping("/users/{userId}")
    public BaseResponseDto<UserResponseDtoV2> getUser(@PathVariable Long userId){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        UserResponseDtoV2 userResponseDtoV2 = UserResponseDtoV2.builder()
                .userId(currentUserV2.getUserId())
                .email(currentUserV2.getEmail())
                .nickname(currentUserV2.getNickname())
                .profileImage(currentUserV2.getProfileImage())
                .selfDescription(currentUserV2.getSelfDescription())
                .categories(currentUserV2.getCategories().stream()
                        .map(category -> category.toDto(category))
                        .collect(Collectors.toList()))
                .build();

        return BaseResponseDto.ofSuccess(GET_USER_SUCCESS, userResponseDtoV2);
    }

    // 유저 프로필 수정 (닉네임, 한줄 소개, 프로필사진)
    @PutMapping(value = "/users/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<UserResponseDtoV2> updateUser(@PathVariable Long userId,
                                                         UpdateUserRequestDto requestUser){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        UserV2 newUserV2 = userServiceV2.updateUser(currentUserV2, requestUser);
        userRepositoryV2.save(newUserV2);

        UserResponseDtoV2 userResponseDtoV2 = UserResponseDtoV2.builder()
                .userId(currentUserV2.getUserId())
                .email(currentUserV2.getEmail())
                .nickname(currentUserV2.getNickname())
                .profileImage(currentUserV2.getProfileImage())
                .selfDescription(currentUserV2.getSelfDescription())
                .categories(currentUserV2.getCategories().stream()
                        .map(category -> category.toDto(category))
                        .collect(Collectors.toList()))
                .build();

        return BaseResponseDto.ofSuccess(MODIFY_USER_SUCCESS, userResponseDtoV2);
    }

    // 임시로 User 추가
    // TODO: 삭제하기
    @PostMapping("/users/join")
    public DataResponseDto<Object> postTestUser(UserV2 userV2) {
        System.out.println("id: " + userV2.getUserId());
        System.out.println("nickname: " + userV2.getNickname());
        System.out.println("email: " + userV2.getEmail());
        System.out.println("profileImage: " + userV2.getProfileImage());
        System.out.println("describe: " + userV2.getSelfDescription());
        System.out.println("created date: " + userV2.getCreatedDate());

        userRepositoryV2.save(userV2);

        //return DataResponseDto.empty();
        return DataResponseDto.of("회원가입에 성공하였습니다.", null);
    }
}