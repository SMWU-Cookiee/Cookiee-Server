package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import com.cookiee.cookieeserver.domain.User;
import lombok.Builder;
import lombok.Getter;

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
                .startTime(startTime)
                .endTime(endTime)
                .eventCategoryList(eventCategories)
                .imageUrl(imageUrls)
                .build();
    }
}
