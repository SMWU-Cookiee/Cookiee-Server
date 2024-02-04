package com.cookiee.cookieeserver.controller;


import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.dto.request.EventGetRequestDto;
import com.cookiee.cookieeserver.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.dto.request.EventUpdateRequestDto;
import com.cookiee.cookieeserver.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.service.EventService;
import com.cookiee.cookieeserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//@Api(tags = "event")
@RestController
@RequiredArgsConstructor
@Controller
public class EventController {
    @Autowired
    private final EventService eventService;
    @Autowired
    private final UserService userService;

    // 등록
    @Operation(summary = "캘린더에서 이벤트 등록")
/*    @ApiImplicitParam(
            name = "userId",
            value = "유저 아이디",
            required = true,
            dataType = "int",
            paramType = "path",
            defaultValue = "None"
    )*/
    @ResponseBody
    @PostMapping(value = "/event/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<EventResponseDto> saveEvent(
            @PathVariable Long userId,
            @RequestParam(value = "images") List<MultipartFile> imageUrl,
            EventRegisterRequestDto eventRegisterRequestDto) throws IOException {
        EventResponseDto event;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                event = eventService.createEvent(imageUrl, eventRegisterRequestDto, userId);
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 등록에 실패하였습니다.");
        }
        return DataResponseDto.of(event, "이벤트 등록에 성공하였습니다.");
    }

    //eventId별 상세조회
    @ResponseBody
    @GetMapping("/event/view/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> getEventDetail(@PathVariable long userId, @PathVariable long eventId) {
        EventResponseDto event;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                event = eventService.getEventDetail(userId,eventId);
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 조회에 실패하였습니다.");
        }
        return DataResponseDto.of(event, "이벤트 조회에 성공하였습니다.");
    }

    //날짜별 상세조회
    @ResponseBody
    @GetMapping("/event/view/{userId}")
    public BaseResponseDto<EventResponseDto> getEventList(@PathVariable long userId, EventGetRequestDto eventGetRequestDto) {
        List<EventResponseDto> events;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                events = eventService.getEventList(userId, eventGetRequestDto);
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 조회에 실패하였습니다.");
        }
        return DataResponseDto.of(events, "이벤트 조회에 성공하였습니다.");
    }


    // 수정
    @ResponseBody
    @PutMapping("/event/update/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> updateEvent(@PathVariable long userId, @PathVariable long eventId, @RequestParam(value = "images", required = false) List<MultipartFile> imageUrl, EventUpdateRequestDto eventUpdateRequestDto) {
        EventResponseDto event;
        try {
            Optional<User> user = userService.findOneById(userId);
            if(user.isEmpty()){
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else{
                event = eventService.updateEvent(userId, eventId,eventUpdateRequestDto, imageUrl);
            }
        } catch ( Exception e){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 수정에 실패하였습니다.");
        }
        return DataResponseDto.of(event, "이벤트 수정에 성공하였습니다.");
    }

    //삭제
    @ResponseBody
    @DeleteMapping("/event/del/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> deleteEvent(@PathVariable long userId, @PathVariable long eventId) {
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 회원이 존재하지 않습니다.");
            } else {
                eventService.deleteEvent(userId, eventId);
                return DataResponseDto.of(null, "이벤트 삭제에 성공하였습니다.");
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 삭제에 실패했습니다");
        }
    }



}
