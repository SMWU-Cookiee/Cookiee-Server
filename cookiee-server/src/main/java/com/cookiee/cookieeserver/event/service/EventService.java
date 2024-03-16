package com.cookiee.cookieeserver.event.service;

import com.amazonaws.services.s3.AmazonS3;
import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.event.dto.request.EventGetRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventUpdateRequestDto;
import com.cookiee.cookieeserver.event.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.category.repository.CategoryRepository;
import com.cookiee.cookieeserver.global.repository.EventCategoryRepository;
import com.cookiee.cookieeserver.event.repository.EventRepository;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

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
    @Autowired
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Transactional
    public EventResponseDto createEvent(List<MultipartFile> images, EventRegisterRequestDto eventRegisterRequestDto, User user){

        List<Category> categoryList = eventRegisterRequestDto.categoryIds().stream()
                .map(
                        // 유저 아이디랑 카테고리 아이디랑 부합해야하므로 findByUserUserIdAndCategoryId로 변경함!
                        id -> categoryRepository.findByUserUserIdAndCategoryId(user.getUserId(), id).orElseThrow(
                                () -> new GeneralException(CATEGORY_NOT_FOUND)
                        )
                )
                .collect(Collectors.toList());

        if (images == null)
            throw new GeneralException(IMAGE_IS_NULL);

        // TODO: 근데 null이 아닌데 이미지 없어도 추가됨
        if (!images.isEmpty()) {
            List<String> storedFileNames = new ArrayList<>();

            for (MultipartFile image : images) {
                String storedFileName = s3Uploader.saveFile(image, String.valueOf(user.getUserId()), "event");
                storedFileNames.add(storedFileName);
                System.out.println(storedFileName);
            }
            Event savedEvent = eventRepository.save(eventRegisterRequestDto.toEntity(user, new ArrayList<EventCategory>(), storedFileNames));
            List<EventCategory> eventCategoryList = categoryList.stream()
                    .map(category ->
                            EventCategory.builder().event(savedEvent).category(category).build()
                    ).collect(Collectors.toList());

            eventCategoryRepository.saveAll(eventCategoryList);
            savedEvent.setEventCategories(eventCategoryList);
            return EventResponseDto.from(savedEvent);
        }
        else {
            throw new GeneralException(IMAGE_IS_NULL);
        }
    }

    @Transactional
    public EventResponseDto getEventDetail(long userId, long eventId){
        // 이것도 optional로 바꿔서 orElseThrow로 유저아이디-이벤트 아이디 부합하는지 확인해야할거같은디..
        Event event = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        return EventResponseDto.from(event);
    }

    @Transactional
    public List<EventResponseDto> getEventList(long userId, EventGetRequestDto eventGetRequestDto){
        List<Event> events = eventRepository.findByUserUserIdAndEventYearAndEventMonthAndEventDate(userId, eventGetRequestDto.eventYear(), eventGetRequestDto.eventMonth(), eventGetRequestDto.eventDate());
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDto updateEvent(long userId, long eventId, EventUpdateRequestDto eventUpdateRequestDto, List<MultipartFile> images) {
        Event updatedEvent = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        if(updatedEvent == null){
            throw new GeneralException(EVENT_NOT_FOUND);
        }

        List<String> imageUrls = updatedEvent.getImageUrl();
        for (String imageUrl : imageUrls){
            String fileName = extractFileNameFromUrl(imageUrl);
            amazonS3Client.deleteObject(bucketName, fileName);
        }

        List<String> storedFileNames = new ArrayList<>();
        for (MultipartFile image : images) {
            String storedFileName = s3Uploader.saveFile(image, String.valueOf(userId), "event");
            storedFileNames.add(storedFileName);
        }

        eventCategoryRepository.deleteAll(updatedEvent.getEventCategories());
        List<Category> categoryList = eventUpdateRequestDto.categoryIds().stream()
                .map(
                        id -> categoryRepository.findByCategoryId(id).orElseThrow(
                                () -> new GeneralException(CATEGORY_NOT_FOUND)
                        )
                )
                .collect(Collectors.toList());
        List<EventCategory> eventCategoryList = categoryList.stream()
                .map(category ->
                        EventCategory.builder().event(updatedEvent).category(category).build()
                ).collect(Collectors.toList());

        eventCategoryRepository.saveAll(eventCategoryList);

        updatedEvent.update(
                eventUpdateRequestDto.eventWhat(),
                eventUpdateRequestDto.eventWhere(),
                eventUpdateRequestDto.withWho(),
                eventUpdateRequestDto.startTime(),
                eventUpdateRequestDto.endTime(),
                storedFileNames,
                eventCategoryList
        );

        return EventResponseDto.from(updatedEvent);
    }


    @Transactional
    public void deleteEvent(long userId, long eventId){
        Event deletedevent;
        deletedevent = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        List<String> imageUrls = deletedevent.getImageUrl();
        List<EventCategory> deleteEventCategories = eventCategoryRepository.findEventCategoriesByEventEventId(eventId);
        for (EventCategory deleteEventCategory : deleteEventCategories){
            eventCategoryRepository.delete(deleteEventCategory);
        }
        for (String imageUrl : imageUrls){
            String fileName = extractFileNameFromUrl(imageUrl);
            amazonS3Client.deleteObject(bucketName, fileName);
        }
        eventRepository.delete(deletedevent);

    }

    @Transactional
    public void deleteAllEvent(Long userId){
        List<Event> eventList = eventRepository.findAllByUserUserId(userId);
        for (Event event: eventList){
            deleteEvent(userId, event.getEventId());
        }
    }

    public static String extractFileNameFromUrl(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

