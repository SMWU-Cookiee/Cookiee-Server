package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class EventServiceImpl extends EventService {
    private final EventRepository eventRepository;

    @Autowired
    private S3Uploader s3Uploader;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override @Transactional
    public Long keepEvent(MultipartFile image, Event event) throws IOException {
        System.out.println("Event service eventDiary");
        if(!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image,"images");
            event.setImageUrl(storedFileName);
        }
        Event savedEvent = eventRepository.save(event);
        return savedEvent.getEvent_id();
    }

}
