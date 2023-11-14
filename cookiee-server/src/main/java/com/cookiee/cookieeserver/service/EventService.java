package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.Event;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class EventService {
    @Transactional
    public abstract Long keepEvent(MultipartFile image, Event event) throws IOException;
}
