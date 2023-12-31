package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.controller.S3Uploader;
import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    @Autowired
    private S3Uploader s3Uploader;

    public EventServiceImpl(EventRepository eventRepository, S3Uploader s3Uploader, UserRepository userRepository, CategoryRepository categoryRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override @Transactional
    public Long createEvent(List<MultipartFile> images, EventRegisterRequestDto eventRegisterRequestDto, Long userId) throws IOException {
        User user = userRepository.findByUserId(userId);
        List<Category> categories = categoryRepository.findAllByEventEventId(eventRegisterRequestDto.eventId());
        if (!images.isEmpty()) {
            List<String> storedFileNames = new ArrayList<>();

            for (MultipartFile image : images) {
                String storedFileName = s3Uploader.saveFile(image);
                eventRegisterRequestDto.toEntity(user, categories).setImageUrl(Collections.singletonList(storedFileName));
            }
        }
        /*String storedFileName1 = s3Uploader.saveFile(thumbnail);
        eventRegisterRequestDto.toEntity(user, categories).setThumbnailUrl(storedFileName1);*/
        Event savedEvent = eventRepository.save(eventRegisterRequestDto.toEntity(user, categories));
        return savedEvent.getEventId();
    }

/*    @Override
    public <T> Optional<T> searchEvent(long userId, long eventId) throws IOException {
        return Optional.empty();
    }*/

/*    @Override @Transactional
    public Long serchEvent(Long userId, Long eventId) throws IOException{


    }*/

}
