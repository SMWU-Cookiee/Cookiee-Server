package com.cookiee.cookieeserver.user.controller;


import com.cookiee.cookieeserver.user.dto.response.UserResponseDtoV1;
import com.cookiee.cookieeserver.user.service.UserServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @PostMapping()
    public ResponseEntity<UserResponseDtoV1> registerUser(@RequestParam("deviceId") String deviceId){
        return ResponseEntity.ok(userServiceV1.registerUser(deviceId));
    }
}
