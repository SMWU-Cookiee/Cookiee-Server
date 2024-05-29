package com.cookiee.cookieeserver.user.controller;


import com.cookiee.cookieeserver.user.dto.response.UserByDeviceResponseDto;
import com.cookiee.cookieeserver.user.service.UserByDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/")
@Tag(name="디바이스 유저 등록", description="디바이스 별 유저를 등록할 수 있습니다.")
public class UserByDeviceController {

    private final UserByDeviceService userByDeviceService;

    @PostMapping("{deviceId}")
    @Operation(summary = "디바이스 유저 등록")
    public ResponseEntity<UserByDeviceResponseDto> registerUser(@PathVariable String deviceId){
        return ResponseEntity.ok(userByDeviceService.registerUser(deviceId));
    }
}
