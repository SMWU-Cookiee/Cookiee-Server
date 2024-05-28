package com.cookiee.cookieeserver.event.controller;

import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.event.dto.request.EventGetRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventUpdateRequestDto;
import com.cookiee.cookieeserver.event.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.event.service.EventUserBySocialLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="api/v2/events/")
@Controller
@Tag(name="이벤트 CRUD", description="(소셜로그인 유저용) 이벤트를 등록/조회/수정/삭제 할 수 있습니다.")
public class EventUserBySocialLoginController {
    @Autowired
    private final EventUserBySocialLoginService eventUserBySocialLoginService;
    @Autowired
    private final JwtService jwtService;

    @ResponseBody
    @Operation(summary = "이벤트 등록")
    @PostMapping(value = "{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<EventResponseDto> saveEvent(@PathVariable Long userId, @RequestParam(value = "images") List<MultipartFile> imageUrl, EventRegisterRequestDto eventRegisterRequestDto) {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event = eventUserBySocialLoginService.createEvent(imageUrl, eventRegisterRequestDto, user);
        return BaseResponseDto.ofSuccess(CREATE_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @Operation(summary = "이벤트 상세 조회")
    @GetMapping("{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> getEventDetail(@PathVariable long userId, @PathVariable long eventId) {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event = eventUserBySocialLoginService.getEventDetail(user.getUserId(),eventId);
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @Operation(summary = "이벤트 목록 조회")
    @GetMapping("{userId}")
    public BaseResponseDto<EventResponseDto> getEventList(@PathVariable long userId, EventGetRequestDto eventGetRequestDto) {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        List<EventResponseDto> events = eventUserBySocialLoginService.getEventList(user.getUserId(), eventGetRequestDto);
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, events);
    }

    @ResponseBody
    @Operation(summary = "이벤트 수정")
    @PutMapping("{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> updateEvent(@PathVariable long userId, @PathVariable long eventId, @RequestParam(value = "images", required = false) List<MultipartFile> imageUrl, EventUpdateRequestDto eventUpdateRequestDto) {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event =  eventUserBySocialLoginService.updateEvent(user.getUserId(), eventId,eventUpdateRequestDto, imageUrl);
        return BaseResponseDto.ofSuccess(MODIFY_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("{userId}/{eventId}")
    public BaseResponseDto<?> deleteEvent(@PathVariable long userId, @PathVariable long eventId) {
        final User user = jwtService.getAndValidateCurrentUser(userId);
        eventUserBySocialLoginService.deleteEvent(user.getUserId(), eventId);
        return BaseResponseDto.ofSuccess(DELETE_EVENT_SUCCESS);
    }
}