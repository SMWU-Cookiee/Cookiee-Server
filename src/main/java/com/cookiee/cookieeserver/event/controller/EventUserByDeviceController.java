package com.cookiee.cookieeserver.event.controller;

import com.cookiee.cookieeserver.event.service.EventUserByDeviceService;
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
@RequestMapping(value="api/v1/events/")
@Controller
@Tag(name="이벤트 CRUD (디바이스 유저)", description="(디바이스 등록 유저용) 이벤트를 등록/조회/수정/삭제 할 수 있습니다.")
public class EventUserByDeviceController {
    @Autowired
    private final EventUserByDeviceService eventUserByDeviceService;

    @ResponseBody
    @Operation(summary = "이벤트 등록")
    @PostMapping(value = "{deviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponseDto<EventResponseDto> saveEvent(@PathVariable String deviceId, @RequestPart(value = "images") List<MultipartFile> imageUrl, @RequestPart("eventDetail")EventRegisterRequestDto eventRegisterRequestDto) {
        EventResponseDto event = eventUserByDeviceService.createEvent(imageUrl, eventRegisterRequestDto, deviceId);
        return BaseResponseDto.ofSuccess(CREATE_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @Operation(summary = "이벤트 상세 조회")
    @GetMapping("{deviceId}/{eventId}")
    public BaseResponseDto<EventResponseDto> getEventDetail(@PathVariable String deviceId, @PathVariable long eventId) {
        EventResponseDto event = eventUserByDeviceService.getEventDetail(deviceId, eventId);
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @Operation(summary = "이벤트 목록 조회")
    @GetMapping("{deviceId}")
    public BaseResponseDto<EventResponseDto> getEventList(@PathVariable String deviceId, EventGetRequestDto eventGetRequestDto) {
        List<EventResponseDto> events = eventUserByDeviceService.getEventList(deviceId, eventGetRequestDto);
        return BaseResponseDto.ofSuccess(GET_EVENT_SUCCESS, events);
    }

    @ResponseBody
    @Operation(summary = "이벤트 수정")
    @PutMapping("{deviceId}/{eventId}")
    public BaseResponseDto<EventResponseDto> updateEvent(@PathVariable String deviceId, @PathVariable long eventId, @RequestPart(value = "images", required = false) List<MultipartFile> imageUrl, @RequestPart("eventDetail")EventUpdateRequestDto eventUpdateRequestDto) {
        EventResponseDto event =  eventUserByDeviceService.updateEvent(deviceId, eventId,eventUpdateRequestDto, imageUrl);
        return BaseResponseDto.ofSuccess(MODIFY_EVENT_SUCCESS, event);
    }

    @ResponseBody
    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("{deviceId}/{eventId}")
    public BaseResponseDto<?> deleteEvent(@PathVariable String deviceId, @PathVariable long eventId) {
        eventUserByDeviceService.deleteEvent(deviceId, eventId);
        return BaseResponseDto.ofSuccess(DELETE_EVENT_SUCCESS);
    }
}