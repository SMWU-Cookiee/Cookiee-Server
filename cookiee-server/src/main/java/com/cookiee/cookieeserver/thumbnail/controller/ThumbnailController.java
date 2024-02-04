package com.cookiee.cookieeserver.thumbnail.controller;

import com.cookiee.cookieeserver.global.StatusCode;
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
import java.util.Optional;

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

    //등록
    @ResponseBody
    @PostMapping(value = "/thumbnail/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<ThumbnailResponseDto> createThumbnail(@PathVariable Long userId,
                                                    HttpServletRequest request,
                                                    @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                    ThumbnailRegisterRequestDto thumbnailRegisterRequestDto) throws IOException {
        ThumbnailResponseDto thumbnail;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                thumbnail = thumbnailService.createThumbnail(thumbnailUrl, thumbnailRegisterRequestDto, userId);
            }
        }
        catch (Exception e){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "썸네일 등록에 실패하였습니다.");
        }
        return DataResponseDto.of(thumbnail, "썸네일 등록에 성공하였습니다.");
    }

    //조회
    @ResponseBody
    @GetMapping(value="/thumbnail/view/{userId}")
    public BaseResponseDto<ThumbnailResponseDto> getThumbnail(@PathVariable Long userId) {
        List<ThumbnailResponseDto> thumbnail;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                thumbnail = thumbnailService.getThumbnail(userId);
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "썸네일 조회에 실패하였습니다.");
        }
        return DataResponseDto.of(thumbnail, "썸네일 조회에 성공하였습니다.");
    }

    //수정
    @ResponseBody
    @PutMapping(value = "/thumbnail/update/{userId}/{thumbnailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<ThumbnailResponseDto> updateThumbnail(@PathVariable long userId, @PathVariable long thumbnailId,
                                                                                  @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                                                  ThumbnailUpdateRequestDto thumbnailUpdateRequestDto) throws IOException {
        ThumbnailResponseDto updated;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                updated = thumbnailService.updateThumbnail(thumbnailUrl, thumbnailUpdateRequestDto, userId, thumbnailId);
            }
        }
        catch (Exception e){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "썸네일 수정에 실패하였습니다.");
        }
        return DataResponseDto.of(updated, "썸네일 수정에 성공하였습니다.");
    }


    //삭제
    @ResponseBody
    @DeleteMapping(value="/thumbnail/del/{userId}/{thumbnailId}")
    public BaseResponseDto deleteThumbnail(@PathVariable Long userId, @PathVariable Long thumbnailId){
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                thumbnailService.deleteThumbnail(userId, thumbnailId);
                return DataResponseDto.of(null, "썸네일 삭제에 성공하였습니다.");
            }
        } catch (Exception e){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "썸네일 삭제에 실패하였습니다.");
        }
    }


}
