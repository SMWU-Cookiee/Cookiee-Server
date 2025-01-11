package com.cookiee.cookieeserver.event.service;

import com.amazonaws.services.s3.AmazonS3;
import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.event.dto.request.EventGetRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventRegisterRequestDto;
import com.cookiee.cookieeserver.event.dto.request.EventUpdateRequestDto;
import com.cookiee.cookieeserver.event.dto.response.EventResponseDto;
import com.cookiee.cookieeserver.category.repository.CategoryRepository;
import com.cookiee.cookieeserver.global.repository.EventCategoryRepository;
import com.cookiee.cookieeserver.event.repository.EventRepository;
import com.cookiee.cookieeserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventUserBySocialLoginService {
    @Autowired
    private final EventRepository eventRepository;
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
    public EventResponseDto createEvent(List<MultipartFile> eventImages, EventRegisterRequestDto eventRegisterRequestDto, User user){

        List<Category> categoryList = eventRegisterRequestDto.categoryIds().stream()
                .map(
                        id -> categoryRepository.findByUserUserIdAndCategoryId(user.getUserId(), id).orElseThrow(
                                () -> new GeneralException(CATEGORY_NOT_FOUND)
                        )
                )
                .collect(Collectors.toList());

        if (!eventImages.isEmpty()) {
            List<String> storedFileNames = new ArrayList<>();

            for (MultipartFile eventImage : eventImages) {
                String storedFileName = s3Uploader.saveFile(eventImage, String.valueOf(user.getUserId()), "event");
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
        Event event = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        if(event.getEventId() == eventId)
            return EventResponseDto.from(event);
        else
            throw new GeneralException(EVENT_NOT_FOUND);
    }

    @Transactional
    public List<EventResponseDto> getEventList(long userId, EventGetRequestDto eventGetRequestDto){
        List<Event> events = eventRepository.findByUserUserIdAndEventYearAndEventMonthAndEventDate(userId, eventGetRequestDto.eventYear(), eventGetRequestDto.eventMonth(), eventGetRequestDto.eventDate());
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDto updateEvent(long userId, long eventId, EventUpdateRequestDto eventUpdateRequestDto, List<MultipartFile> eventImages) {
        Event updatedEvent = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        List<String> storedFileNames = new ArrayList<>();
        if(eventImages != null) {
            List<String> imageUrls = updatedEvent.getImageUrl();
            for (String imageUrl : imageUrls){
                String fileName = extractFileNameFromUrl(imageUrl);
                amazonS3Client.deleteObject(bucketName, fileName);
            }
            for (MultipartFile image : eventImages) {
                String storedFileName = s3Uploader.saveFile(image, String.valueOf(userId), "event");
                storedFileNames.add(storedFileName);
            }}

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
                    eventUpdateRequestDto.eventTitle(),
                    eventUpdateRequestDto.eventWhat(),
                    eventUpdateRequestDto.eventWhere(),
                    null,
                    eventUpdateRequestDto.withWho(),
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

    public static String extractFileNameFromUrl(String eventImageUrl) {
        try {
            URI uri = new URI(eventImageUrl);
            String path = uri.getPath();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}