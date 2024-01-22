package com.cookiee.cookieeserver.controller;


import com.cookiee.cookieeserver.constant.StatusCode;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.cookiee.cookieeserver.service.EventService;
import com.cookiee.cookieeserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

//@Api(tags = "event")
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
    public BaseResponseDto<Event> saveEvent(
            @PathVariable int userId, HttpServletRequest request,
            @RequestParam(value = "images") List<MultipartFile> imageUrl,
            EventRegisterRequestDto eventRegisterRequestDto) throws IOException {
        Event event;
        try {
            Optional<User> user = userService.findOneById(userId);
            if (user.isEmpty()) {
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            } else {
                event = eventService.createEvent(imageUrl, eventRegisterRequestDto, (long) userId);
            }
        } catch (Exception e) {
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "이벤트 등록에 실패하였습니다.");
        }
        return DataResponseDto.of(event, "이벤트 등록에 성공하였습니다.");
    }
}




   /* @ResponseBody
    @PostMapping(value = "/event/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveEvent(@PathVariable long userId, HttpServletRequest request, @RequestParam(value = "image") MultipartFile image, Event event) throws IOException {
        Long eventId = eventService.createEvent(image, event, userId);
        return eventId;
    }*/


/*    @GetMapping("/event")
    public HttpResponse<Optional<EventResponseDto>> getEvent(@RequestParam long userId, @RequestParam long eventId) {
        return HttpResponse.okBuild(
                eventService.searchEvent(userId, eventId)
                        .map((Event event) -> EventResponseDto.from(event)));
    }

    Event event = eventRepository.findById(eventId).orElseThrow(new Supplier<IllegalArgumentException>() {
        @Override
        public IllegalArgumentException get() {
            return new IllegalArgumentException("해당 이벤트는 존재하지 않습니다. id: " + eventId);
        }
    });

        return DataResponseDto.of(event);
}*/


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

