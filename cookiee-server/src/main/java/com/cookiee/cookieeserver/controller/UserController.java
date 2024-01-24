package com.cookiee.cookieeserver.controller;

import com.cookiee.cookieeserver.constant.StatusCode;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    // health check에 대한 상태 반환 위해서
    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
    // 유저 프로필 조회
    @GetMapping("/users/{userId}")
    public BaseResponseDto<User> getUser(@PathVariable Long userId){
        Optional<User> user;

//        try{
//
//        }
//        catch (IllegalArgumentException e){
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
//        }
//
//        return DataResponseDto.of(user);
        user = userRepository.findById(userId);
        if(user.isEmpty()){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
        }
        else{
            return DataResponseDto.of(user, "회원 정보 조회 요청에 성공하였습니다.");
        }
    }

    // 유저 프로필 수정 (닉네임, 한줄 소개, 프로필사진)
    @Transactional
    @PutMapping("/users/{userId}")
    public BaseResponseDto<User> updateUser(@PathVariable Long userId, @RequestBody User requestUser){
//        User newUser = userRepository.findById(userId).orElseThrow(new Supplier<IllegalArgumentException>() {
//            @Override
//            public IllegalArgumentException get() {
//                return new IllegalArgumentException("프로필 수정에 실패하였습니다.");
//            }
//        });
//        newUser.setNickname(requestUser.getNickname());
//        newUser.setSelfDescription(requestUser.getSelfDescription());
//        newUser.setProfileImage(requestUser.getProfileImage());

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
        }
        else{
            try{
                user.ifPresent(it -> {
                    it.setNickname(requestUser.getNickname());
                    it.setSelfDescription(requestUser.getSelfDescription());
                    it.setProfileImage(requestUser.getProfileImage());
                });
                return DataResponseDto.of(user, "회원 정보를 성공적으로 수정하였습니다.");
            }
            catch (Exception e){
                return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, "회원 정보 수정에 실패하였습니다.");
            }
        }
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
