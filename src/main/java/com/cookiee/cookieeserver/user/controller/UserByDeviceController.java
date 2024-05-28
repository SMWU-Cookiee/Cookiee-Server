package com.cookiee.cookieeserver.user.controller;


import com.cookiee.cookieeserver.user.dto.response.UserByDeviceResponseDto;
import com.cookiee.cookieeserver.user.service.UserByDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/")
public class UserByDeviceController {

    private final UserByDeviceService userByDeviceService;

    @PostMapping("{deviceId}")
    public ResponseEntity<UserByDeviceResponseDto> registerUser(@PathVariable String deviceId){
        return ResponseEntity.ok(userByDeviceService.registerUser(deviceId));
    }
}
