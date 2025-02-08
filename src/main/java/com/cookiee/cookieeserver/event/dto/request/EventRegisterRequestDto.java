package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.event.domain.Place;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.global.domain.EventWhereType;
import com.cookiee.cookieeserver.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record EventRegisterRequestDto (
    String eventTitle,
    String eventWhat,
    String eventWhereText,
    EventPlaceRequestDto eventWherePlace,
    String withWho,
    int eventYear,
    int eventMonth,
    int eventDate,
    List<Long> categoryIds){

    public Event toEntity(User user, List<EventCategory> eventCategories, List<String> imageUrls){
        Place eventPlace = (eventWherePlace != null) ? eventWherePlace.toEntity() : null;
        return Event.builder()
                .eventTitle(eventTitle)
                .eventWhat(eventWhat)
                .eventWherePlace(eventPlace)
                .eventWhereText(eventWhereText)
                .withWho(withWho)
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .user(user)
                .eventCategoryList(eventCategories)
                .imageUrl(imageUrls)
                .build();
    }

}
