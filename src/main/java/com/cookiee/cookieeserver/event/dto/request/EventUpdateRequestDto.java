package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;

import java.util.List;

public record EventUpdateRequestDto(
        String eventWhat,
        String eventWhere,
        String withWho,
        String startTime,
        String endTime,
        List<Long> categoryIds){

    public Event toEntity(List<EventCategory> eventCategories, List<String> imageUrls){
        return Event.builder()
                .eventWhat(eventWhat)
                .eventWhere(eventWhere)
                .withWho(withWho)
                .eventCategoryList(eventCategories)
                .imageUrl(imageUrls)
                .build();
    }
}
