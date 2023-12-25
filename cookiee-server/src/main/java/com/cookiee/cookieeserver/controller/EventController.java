package com.cookiee.cookieeserver.controller;




import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.EventService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@Controller
public class EventController {
    @Autowired
    private final EventService eventService;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;

    // 등록
    @ResponseBody
    @PostMapping(value = "/event/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveEvent(@PathVariable long userId, HttpServletRequest request, @RequestParam(value = "images") List<MultipartFile> imageUrl, @RequestParam(value = "thumbnail") MultipartFile thumbnail, EventRegisterRequestDto eventRegisterRequestDto) throws IOException {
        Long eventId = eventService.createEvent(imageUrl, thumbnail, eventRegisterRequestDto, userId);
        return eventId;
    }




   /* @ResponseBody
    @PostMapping(value = "/event/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveEvent(@PathVariable long userId, HttpServletRequest request, @RequestParam(value = "image") MultipartFile image, Event event) throws IOException {
        Long eventId = eventService.createEvent(image, event, userId);
        return eventId;
    }*/

/*    // 날짜별 조회
    @GetMapping("/event")
    public HttpResponse<Optional<EventResponseDto>> getEvent(@RequestParam long userId, @RequestParam long eventId) {
        return HttpResponse.okBuild(
                eventService.searchEvent(userId, eventId)
                        .map((Event event) -> EventResponseDto.from(event)));
    }*/
       /* Event event = eventRepository.findById(eventId).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 이벤트는 존재하지 않습니다. id: " + eventId);
            }
        });*/
/*
        return DataResponseDto.of(event);*/
    }


  /*  // 수정
    @Transactional
    @PutMapping("/users/{userId}/{eventId}")
    public DataResponseDto<Event> updateEvent(@PathVariable long eventId, @RequestBody Event requestEvent){
        Event newEvent = eventRepository.findById(eventId).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("이벤트 수정에 실패하였습니다.");
            }
        });
        newEvent.setWhat(requestEvent.getWhat());
        newEvent.setWhen(requestEvent.getWhen());
        newEvent.setImageUrl(requestEvent.getImageUrl());
        newEvent.setWith_who(requestEvent.getWith_who());
        newEvent.setCategories(requestEvent.getCategories());

        return DataResponseDto.of(newEvent, "이벤트를 성공적으로 수정하였습니다.");
    }*/

