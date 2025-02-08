package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.event.domain.Place;
import com.cookiee.cookieeserver.global.domain.EventCategory;

import java.util.List;

public record EventUpdateRequestDto(
        String eventTitle,
        String eventWhat,
        String eventWhereText,
        EventPlaceRequestDto eventWherePlace,
        String withWho,
        List<Long> categoryIds){

    public Event toEntity(List<EventCategory> eventCategories, List<String> imageUrls){
        Place eventPlace = (eventWherePlace != null) ? eventWherePlace.toEntity() : null;
        return Event.builder()
                .eventTitle(eventTitle)
                .eventWhat(eventWhat)
                .eventWhereText(eventWhereText)
                .eventWherePlace(eventPlace)
                .withWho(withWho)
                .eventCategoryList(eventCategories)
                .imageUrl(imageUrls)
                .build();
    }
}
