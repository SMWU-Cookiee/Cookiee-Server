package com.cookiee.cookieeserver.controller;

import com.cookiee.cookieeserver.constant.StatusCode;
import com.cookiee.cookieeserver.domain.Thumbnail;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.dto.response.ThumbnailGetResponseDto;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.EventService;
import com.cookiee.cookieeserver.service.ThumbnailService;
import com.cookiee.cookieeserver.service.UserService;
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
    public BaseResponseDto<ThumbnailRegisterRequestDto> createThumbnail(@PathVariable int userId,
                                                    HttpServletRequest request,
                                                    @RequestParam(value = "thumbnail") MultipartFile thumbnailUrl,
                                                    ThumbnailRegisterRequestDto thumbnailRegisterRequestDto) throws IOException {
        Thumbnail thumbnail;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                thumbnail = thumbnailService.createThumbnail(thumbnailUrl, thumbnailRegisterRequestDto, (long) userId);
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
    public BaseResponseDto<ThumbnailGetResponseDto> getThumbnail(@PathVariable int userId) {
        List<ThumbnailGetResponseDto> thumbnail;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                thumbnail = thumbnailService.getThumbnail((long) userId);
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "썸네일 조회에 실패하였습니다.");
        }
        return DataResponseDto.of(thumbnail, "썸네일 조회에 성공하였습니다.");
    }

    //삭제
    @ResponseBody
    @DeleteMapping(value="/thumbnail/del/{userId}/{thumbnailId}")
    public BaseResponseDto deleteThumbnail(@PathVariable Long userId, @PathVariable Long thumbnailId){
        try {
            Optional<User> user = userService.findOneById(Math.toIntExact(userId));
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
