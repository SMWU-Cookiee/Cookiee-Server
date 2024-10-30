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
import com.cookiee.cookieeserver.user.repository.UserRepository;
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
public class EventUserByDeviceService {
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final EventCategoryRepository eventCategoryRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private S3Uploader s3Uploader;
    @Autowired
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Transactional
    public EventResponseDto createEvent(List<MultipartFile> eventImages, EventRegisterRequestDto eventRegisterRequestDto, String deviceId){
        User user = getUserByDeviceId(deviceId);

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
    public EventResponseDto getEventDetail(String deviceId, long eventId){
        User user = getUserByDeviceId(deviceId);
        Long userId = user.getUserId();
        Event event = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        if(event.getEventId() == eventId)
            return EventResponseDto.from(event);
        else
            throw new GeneralException(EVENT_NOT_FOUND);
    }

    @Transactional
    public List<EventResponseDto> getEventList(String deviceId, EventGetRequestDto eventGetRequestDto){
        User user = getUserByDeviceId(deviceId);
        Long userId = user.getUserId();
        List<Event> events = eventRepository.findByUserUserIdAndEventYearAndEventMonthAndEventDate(userId, eventGetRequestDto.eventYear(), eventGetRequestDto.eventMonth(), eventGetRequestDto.eventDate());
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDto updateEvent(String deviceId, long eventId, EventUpdateRequestDto eventUpdateRequestDto, List<MultipartFile> eventImanges) {
        User user = getUserByDeviceId(deviceId);
        Long userId = user.getUserId();
        Event updatedEvent = eventRepository.findByUserUserIdAndEventId(userId, eventId);
        if(eventImanges != null) {
            List<String> imageUrls = updatedEvent.getImageUrl();
            for (String imageUrl : imageUrls){
                String fileName = extractFileNameFromUrl(imageUrl);
                amazonS3Client.deleteObject(bucketName, fileName);
            }

            List<String> storedFileNames = new ArrayList<>();
            for (MultipartFile image : eventImanges) {
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
                    eventUpdateRequestDto.eventTitle(),
                    eventUpdateRequestDto.eventWhat(),
                    eventUpdateRequestDto.eventWhere(),
                    eventUpdateRequestDto.withWho(),
                    storedFileNames,
                    eventCategoryList
            );

            return EventResponseDto.from(updatedEvent);

        } else
            throw new GeneralException(EVENT_NOT_FOUND);

    }


    @Transactional
    public void deleteEvent(String deviceId, long eventId){
        User user = getUserByDeviceId(deviceId);
        Long userId = user.getUserId();
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
    public void deleteAllEvent(String deviceId){
        User user = getUserByDeviceId(deviceId);
        Long userId = user.getUserId();
        List<Event> eventList = eventRepository.findAllByUserUserId(userId);
        for (Event event: eventList){
            deleteEvent(deviceId, event.getEventId());
        }
    }

    public static String extractFileNameFromUrl(String EventImageUrl) {
        try {
            URI uri = new URI(EventImageUrl);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByDeviceId(String deviceId) {
        return userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));
    }


}