package com.cookiee.cookieeserver.thumbnail.controller;

import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.thumbnail.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.thumbnail.dto.response.ThumbnailResponseDto;
import com.cookiee.cookieeserver.thumbnail.service.ThumbnailUserByDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="api/v1/thumbnails")
@Controller
@Tag(name="썸네일 CRUD (디바이스 유저)", description="(디바이스 유저 전용) 썸네일을 등록/조회/수정/삭제 할 수 있습니다. ")
public class ThumbnailUserByDeviceController {

    @Autowired
    private final ThumbnailUserByDeviceService thumbnailUserByDeviceService;
    @Autowired
    private final JwtService jwtService;

    @ResponseBody
    @PostMapping(value = "{deviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "썸네일 등록")
    public BaseResponseDto<ThumbnailResponseDto> createThumbnail(@PathVariable String deviceId,
                                                                 @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                                 ThumbnailRegisterRequestDto thumbnailRegisterRequestDto) throws IOException {
        ThumbnailResponseDto thumbnail = thumbnailUserByDeviceService.createThumbnail(thumbnailUrl, thumbnailRegisterRequestDto, deviceId);
        return BaseResponseDto.ofSuccess(CREATE_THUMBNAIL_SUCCESS, thumbnail);
    }

    @ResponseBody
    @GetMapping(value="{deviceId}")
    @Operation(summary = "썸네일 조회")
    public BaseResponseDto<ThumbnailResponseDto> getThumbnail(@PathVariable String deviceId) {
        List<ThumbnailResponseDto> thumbnail = thumbnailUserByDeviceService.getThumbnail(deviceId);
        return BaseResponseDto.ofSuccess(GET_THUMBNAIL_SUCCESS, thumbnail);
    }

    @ResponseBody
    @PutMapping(value = "{deviceId}/{thumbnailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "썸네일 수정")
    public BaseResponseDto<ThumbnailResponseDto> updateThumbnail(@PathVariable String deviceId, @PathVariable long thumbnailId,
                                                                 @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl) throws IOException {
        ThumbnailResponseDto updated = thumbnailUserByDeviceService.updateThumbnail(thumbnailUrl, deviceId, thumbnailId);
        return BaseResponseDto.ofSuccess(MODIFY_THUMBNAIL_SUCCESS, updated);
    }


    //삭제
    @ResponseBody
    @DeleteMapping(value="{deviceId}/{thumbnailId}")
    @Operation(summary = "썸네일 삭제")
    public BaseResponseDto<?> deleteOneThumbnail(@PathVariable String deviceId, @PathVariable Long thumbnailId){
        thumbnailUserByDeviceService.deleteThumbnail(deviceId, thumbnailId);
        return BaseResponseDto.ofSuccess(DELETE_THUMBNAIL_SUCCESS);
    }
}