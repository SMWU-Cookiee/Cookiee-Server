package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.controller.S3Uploader;
import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import com.cookiee.cookieeserver.repository.EventCategoryRepository;
import com.cookiee.cookieeserver.repository.EventRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService  {
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final EventCategoryRepository eventCategoryRepository;
    @Autowired
    private S3Uploader s3Uploader;


    @Transactional
    public EventResponseDto createEvent(List<MultipartFile> images, EventRegisterRequestDto eventRegisterRequestDto, Long userId) throws IOException {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 사용자가 없습니다.")
        );


        List<Category> categoryList = eventRegisterRequestDto.categoryIds().stream()
                .map(
                        id -> categoryRepository.findByCategoryId(id).orElseThrow(
                                () -> new IllegalArgumentException("해당 id의 카테고리 없습니다...")
                        )
                )
                .collect(Collectors.toList());

        //List<Category> categoryList = categoryRepository.findAllByCategoryIdList(eventRegisterRequestDto.categoryIds());
        if (!images.isEmpty()) {
            List<String> storedFileNames = new ArrayList<>();

            for (MultipartFile image : images) {
                String storedFileName = s3Uploader.saveFile(image);
                System.out.println(storedFileName);
                storedFileNames.add(storedFileName);
            }
            Event savedEvent = eventRepository.save(eventRegisterRequestDto.toEntity(user, new ArrayList<EventCategory>(), storedFileNames));
            List<EventCategory> eventCategoryList = categoryList.stream()
                    .map(category ->
                            EventCategory.builder().event(savedEvent).category(category).build()
                    ).collect(Collectors.toList());

            eventCategoryRepository.saveAll(eventCategoryList);
            return new EventResponseDto(savedEvent.getEventId(), savedEvent.getEventWhat(), savedEvent.getEventWhere(), savedEvent.getWithWho(), savedEvent.getEventYear(), savedEvent.getEventMonth(), savedEvent.getEventDate(), savedEvent.getImageUrl(), savedEvent.getUser().getUserId(), savedEvent.getEventCategories());
        }

        throw new NullPointerException("사진이 없습니다.");
    }

/*    @Override
    public <T> Optional<T> searchEvent(long userId, long eventId) throws IOException {
        return Optional.empty();
    }*/

/*    @Override @Transactional
    public Long serchEvent(Long userId, Long eventId) throws IOException{


    }*/

}
