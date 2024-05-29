package com.cookiee.cookieeserver.thumbnail.controller;

import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.thumbnail.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.thumbnail.dto.response.ThumbnailResponseDto;
import com.cookiee.cookieeserver.thumbnail.service.ThumbnailUserBySocialLoginService;
import com.cookiee.cookieeserver.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping(value="api/v2/thumbnails")
@Controller
@Tag(name="썸네일 CRUD (소셜로그인 유저)", description="(소셜로그인 유저 전용) 썸네일을 등록/조회/수정/삭제 할 수 있습니다. ")
public class ThumbnailUserBySocialLoginController {

    @Autowired
    private final ThumbnailUserBySocialLoginService thumbnailUserBySocialLoginService;
    @Autowired
    private final JwtService jwtService;

    @ResponseBody
    @PostMapping(value = "{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "썸네일 등록")
    public BaseResponseDto<ThumbnailResponseDto> createThumbnail(@PathVariable Long userId,
                                                                 HttpServletRequest request,
                                                                 @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                                 ThumbnailRegisterRequestDto thumbnailRegisterRequestDto) throws IOException {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);
        ThumbnailResponseDto thumbnail = thumbnailUserBySocialLoginService.createThumbnail(thumbnailUrl, thumbnailRegisterRequestDto, currentUser.getUserId());
        return BaseResponseDto.ofSuccess(CREATE_THUMBNAIL_SUCCESS, thumbnail);
    }

    @ResponseBody
    @GetMapping(value="{userId}")
    @Operation(summary = "썸네일 조회")
    public BaseResponseDto<ThumbnailResponseDto> getThumbnail(@PathVariable Long userId) {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        List<ThumbnailResponseDto> thumbnail = thumbnailUserBySocialLoginService.getThumbnail(user.getUserId());
        return BaseResponseDto.ofSuccess(GET_THUMBNAIL_SUCCESS, thumbnail);
    }

    @ResponseBody
    @PutMapping(value = "{userId}/{thumbnailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "썸네일 수정")
    public BaseResponseDto<ThumbnailResponseDto> updateThumbnail(@PathVariable long userId, @PathVariable long thumbnailId,
                                                                 @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl) throws IOException {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        ThumbnailResponseDto updated = thumbnailUserBySocialLoginService.updateThumbnail(thumbnailUrl, user.getUserId(), thumbnailId);
        return BaseResponseDto.ofSuccess(MODIFY_THUMBNAIL_SUCCESS, updated);
    }

    @ResponseBody
    @DeleteMapping(value="{userId}/{thumbnailId}")
    @Operation(summary = "썸네일 삭제")
    public BaseResponseDto<?> deleteOneThumbnail(@PathVariable Long userId, @PathVariable Long thumbnailId){
        final User user = jwtService.getAndValidateCurrentUser(userId);
        thumbnailUserBySocialLoginService.deleteThumbnail(user.getUserId(), thumbnailId);
        return BaseResponseDto.ofSuccess(DELETE_THUMBNAIL_SUCCESS);
    }
}