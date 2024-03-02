package com.cookiee.cookieeserver.event.controller;

import com.cookiee.cookieeserver.global.StatusCode;
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
        EventResponseDto event;

            User user = userService.findOneById(userId);
            event = eventService.createEvent(imageUrl, eventRegisterRequestDto, user.getUserId());
        return BaseResponseDto.ofSuccess(CREATE_EVENT_SUCCESS, event);
    }

    //eventId별 상세조회
    @ResponseBody
    @GetMapping("/event/view/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> getEventDetail(@PathVariable long userId, @PathVariable long eventId) {
        EventResponseDto event;
//        try {
            User user = userService.findOneById(userId);
            event = eventService.getEventDetail(user.getUserId(),eventId);
//        } catch (Exception e) {
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 조회에 실패하였습니다.");
//        }
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, event);
    }

    //날짜별 상세조회
    @ResponseBody
    @GetMapping("/event/view/{userId}")
    public BaseResponseDto<EventResponseDto> getEventList(@PathVariable long userId, EventGetRequestDto eventGetRequestDto) {
        List<EventResponseDto> events;
//        try {
            User user = userService.findOneById(userId);
            events = eventService.getEventList(user.getUserId(), eventGetRequestDto);
//        } catch (Exception e) {
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 조회에 실패하였습니다.");
//        }
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, events);
    }


    // 수정
    @ResponseBody
    @PutMapping("/event/update/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> updateEvent(@PathVariable long userId, @PathVariable long eventId, @RequestParam(value = "images", required = false) List<MultipartFile> imageUrl, EventUpdateRequestDto eventUpdateRequestDto) {
        EventResponseDto event;
//        try {
            User user = userService.findOneById(userId);
            event = eventService.updateEvent(user.getUserId(), eventId,eventUpdateRequestDto, imageUrl);
//        } catch ( Exception e){
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 수정에 실패하였습니다.");
//        }
        return BaseResponseDto.ofSuccess(MODIFY_EVENT_SUCCESS, event);
    }

    //삭제
    @ResponseBody
    @DeleteMapping("/event/del/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> deleteEvent(@PathVariable long userId, @PathVariable long eventId) {
//        try {
            User user = userService.findOneById(userId);
            eventService.deleteEvent(user.getUserId(), eventId);
            return BaseResponseDto.ofSuccess(DELETE_EVENT_SUCCESS, null);
//        } catch (Exception e) {
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 삭제에 실패했습니다");
//        }
    }

}
