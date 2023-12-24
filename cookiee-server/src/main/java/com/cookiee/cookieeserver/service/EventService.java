package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface EventService {
    Long createEvent(MultipartFile image, Event event, Long UserId) throws IOException;

    <T> Optional<T> searchEvent(long userId, long eventId) throws IOException;
}
