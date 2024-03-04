package com.cookiee.cookieeserver.thumbnail.controller;

import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.thumbnail.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.thumbnail.dto.request.ThumbnailUpdateRequestDto;
import com.cookiee.cookieeserver.thumbnail.dto.response.ThumbnailResponseDto;
import com.cookiee.cookieeserver.event.repository.EventRepository;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import com.cookiee.cookieeserver.event.service.EventService;
import com.cookiee.cookieeserver.thumbnail.service.ThumbnailService;
import com.cookiee.cookieeserver.user.service.UserService;
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
@Controller
public class ThumbnailController {

    @Autowired
    private final EventService eventService;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ThumbnailService thumbnailService;

    private final JwtService jwtService;

    //등록
    @ResponseBody
    @PostMapping(value = "/thumbnail/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<ThumbnailResponseDto> createThumbnail(@PathVariable Long userId,
                                                    HttpServletRequest request,
                                                    @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                    ThumbnailRegisterRequestDto thumbnailRegisterRequestDto) throws IOException {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);
        ThumbnailResponseDto thumbnail;
        thumbnail = thumbnailService.createThumbnail(thumbnailUrl, thumbnailRegisterRequestDto, currentUser.getUserId());
        return BaseResponseDto.ofSuccess(CREATE_THUMBNAIL_SUCCESS, thumbnail);
    }

    //조회
    @ResponseBody
    @GetMapping(value="/thumbnail/view/{userId}")
    public BaseResponseDto<ThumbnailResponseDto> getThumbnail(@PathVariable Long userId) {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        List<ThumbnailResponseDto> thumbnail;
        thumbnail = thumbnailService.getThumbnail(currentUser.getUserId());
        return BaseResponseDto.ofSuccess(GET_THUMBNAIL_SUCCESS, thumbnail);
    }

    //수정
    @ResponseBody
    @PutMapping(value = "/thumbnail/update/{userId}/{thumbnailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<ThumbnailResponseDto> updateThumbnail(@PathVariable long userId, @PathVariable long thumbnailId,
                                                                                  @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                                                  ThumbnailUpdateRequestDto thumbnailUpdateRequestDto) throws IOException {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);
        ThumbnailResponseDto updated;
        updated = thumbnailService.updateThumbnail(thumbnailUrl, thumbnailUpdateRequestDto, currentUser.getUserId(), thumbnailId);
        return BaseResponseDto.ofSuccess(MODIFY_THUMBNAIL_SUCCESS, updated);
    }


    //삭제
    @ResponseBody
    @DeleteMapping(value="/thumbnail/del/{userId}/{thumbnailId}")
    public BaseResponseDto<?> deleteThumbnail(@PathVariable Long userId, @PathVariable Long thumbnailId){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);
        thumbnailService.deleteThumbnail(currentUser.getUserId(), thumbnailId);
        return BaseResponseDto.ofSuccess(DELETE_THUMBNAIL_SUCCESS);
    }
}
