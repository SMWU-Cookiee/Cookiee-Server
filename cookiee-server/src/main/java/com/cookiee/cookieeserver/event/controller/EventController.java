package com.cookiee.cookieeserver.event.controller;

import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.event.dto.request.EventGetRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventUpdateRequestDto;
import com.cookiee.cookieeserver.event.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.event.service.EventService;
import com.cookiee.cookieeserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

//@Api(tags = "event")
@RestController
@RequiredArgsConstructor
@Controller
public class EventController {
    @Autowired
    private final EventService eventService;
    @Autowired
    private final UserService userService;

    private final JwtService jwtService;

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
            EventRegisterRequestDto eventRegisterRequestDto) {

        // 액세스 토큰 정보에 해당하는 유저 가져오기
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        EventResponseDto event;
        event = eventService.createEvent(imageUrl, eventRegisterRequestDto, currentUser);
        return BaseResponseDto.ofSuccess(CREATE_EVENT_SUCCESS, event);
    }

    //eventId별 상세조회
    @ResponseBody
    @GetMapping("/event/view/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> getEventDetail(@PathVariable long userId, @PathVariable long eventId) {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event;
        event = eventService.getEventDetail(currentUser.getUserId(),eventId);

        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, event);
    }

    //날짜별 상세조회
    @ResponseBody
    @GetMapping("/event/view/{userId}")
    public BaseResponseDto<EventResponseDto> getEventList(@PathVariable long userId, EventGetRequestDto eventGetRequestDto) {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);
        List<EventResponseDto> events;

        events = eventService.getEventList(currentUser.getUserId(), eventGetRequestDto);

        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, events);
    }


    // 수정
    @ResponseBody
    @PutMapping("/event/update/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> updateEvent(@PathVariable long userId, @PathVariable long eventId, @RequestParam(value = "images", required = false) List<MultipartFile> imageUrl, EventUpdateRequestDto eventUpdateRequestDto) {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        EventResponseDto event;
        event = eventService.updateEvent(currentUser.getUserId(), eventId,eventUpdateRequestDto, imageUrl);

        return BaseResponseDto.ofSuccess(MODIFY_EVENT_SUCCESS, event);
    }

    //삭제
    @ResponseBody
    @DeleteMapping("/event/del/{userId}/{eventId}")
    public BaseResponseDto<?> deleteEvent(@PathVariable long userId, @PathVariable long eventId) {
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        eventService.deleteEvent(currentUser.getUserId(), eventId);
        return BaseResponseDto.ofSuccess(DELETE_EVENT_SUCCESS);
    }

}
