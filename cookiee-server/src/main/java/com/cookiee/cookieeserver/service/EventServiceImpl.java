package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.controller.S3Uploader;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    private S3Uploader s3Uploader;

    public EventServiceImpl(EventRepository eventRepository, S3Uploader s3Uploader, UserRepository userRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override @Transactional
    public Long createEvent(MultipartFile image, Event event, Long userId) throws IOException {
        if(!image.isEmpty()) {
            String storedFileName = s3Uploader.saveFile(image);
            event.setImageUrl(storedFileName);
        }
        Optional<User> user = userRepository.findByUserId(userId);
        Event savedEvent = eventRepository.save(event);
        return savedEvent.getEvent_id();
    }

    @Override @Transactional
    public Long serchEvent(Long userId, Long eventId) throws IOException{


    }

}
