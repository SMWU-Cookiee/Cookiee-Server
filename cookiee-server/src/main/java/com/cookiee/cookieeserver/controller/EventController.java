/*


package com.cookiee.cookieeserver.controller;




import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.EventService;
import com.cookiee.cookieeserver.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class EventController {
    @Autowired
    private final EventService eventService;
    @Autowired
    private final EventRepository eventRepository;

    // 조회
    @GetMapping("/calendar/{userId}")
    public DataResponseDto<Event> getEvent(@PathVariable long eventId){
        Event event = eventRepository.findById(eventId).orElseThrow(new Supplier<IllegalArgumentException>(){
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 이벤트는 존재하지 않습니다. id: " + eventId);
            }
        });

        return DataResponseDto.of(event);
    }

    // 수정
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

