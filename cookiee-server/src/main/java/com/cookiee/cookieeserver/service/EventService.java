package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.dto.response.EventResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {

    EventResponseDto createEvent(List<MultipartFile> image, EventRegisterRequestDto eventRegisterRequestDto, Long UserId) throws IOException;


/*
    <T> Optional<T> searchEvent(long userId, long eventId) throws IOException;
*/
}
