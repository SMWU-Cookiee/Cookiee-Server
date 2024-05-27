package com.cookiee.cookieeserver.event.controller;

import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.UserV2;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.event.dto.request.EventGetRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventUpdateRequestDto;
import com.cookiee.cookieeserver.event.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.event.service.EventServiceV2;
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
@Controller
public class EventControllerV2 {
    @Autowired
    private final EventServiceV2 eventServiceV2;
    @Autowired
    private final JwtService jwtService;

    @ResponseBody
    @PostMapping(value = "/event/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<EventResponseDto> saveEvent(@PathVariable Long userId, @RequestParam(value = "images") List<MultipartFile> imageUrl, EventRegisterRequestDto eventRegisterRequestDto) {
        final UserV2 userV2 = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event = eventServiceV2.createEvent(imageUrl, eventRegisterRequestDto, userV2);
        return BaseResponseDto.ofSuccess(CREATE_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @GetMapping("/event/view/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> getEventDetail(@PathVariable long userId, @PathVariable long eventId) {
        final UserV2 userV2 = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event = eventServiceV2.getEventDetail(userV2.getUserId(),eventId);
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @GetMapping("/event/view/{userId}")
    public BaseResponseDto<EventResponseDto> getEventList(@PathVariable long userId, EventGetRequestDto eventGetRequestDto) {
        final UserV2 userV2 = jwtService.getAndValidateCurrentUser(userId);
        List<EventResponseDto> events = eventServiceV2.getEventList(userV2.getUserId(), eventGetRequestDto);
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, events);
    }

    @ResponseBody
    @PutMapping("/event/update/{userId}/{eventId}")
    public BaseResponseDto<EventResponseDto> updateEvent(@PathVariable long userId, @PathVariable long eventId, @RequestParam(value = "images", required = false) List<MultipartFile> imageUrl, EventUpdateRequestDto eventUpdateRequestDto) {
        final UserV2 userV2 = jwtService.getAndValidateCurrentUser(userId);
        EventResponseDto event =  eventServiceV2.updateEvent(userV2.getUserId(), eventId,eventUpdateRequestDto, imageUrl);
        return BaseResponseDto.ofSuccess(MODIFY_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @DeleteMapping("/event/del/{userId}/{eventId}")
    public BaseResponseDto<?> deleteEvent(@PathVariable long userId, @PathVariable long eventId) {
        final UserV2 userV2 = jwtService.getAndValidateCurrentUser(userId);
        eventServiceV2.deleteEvent(userV2.getUserId(), eventId);
        return BaseResponseDto.ofSuccess(DELETE_EVENT_SUCCESS);
    }
}