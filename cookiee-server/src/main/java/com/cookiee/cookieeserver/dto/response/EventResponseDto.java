
package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Category;
//import com.cookiee.cookieeserver.domain.Collection;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import lombok.Builder;

import java.util.List;
import java.util.Locale;

@Builder
public record EventResponseDto(
        Long eventId,
        String what,
        String eventWhere,
        String withWho,
        int EventYear,
        int EventMonth,
        int EventDate,
        List<String> imageUrlList,
        Long userId,
        List<EventCategory> Eventcategories){

    public static EventResponseDto from(Event event){
        return EventResponseDto.builder()
                .eventId(event.getEventId())
                .what(event.getEventWhat())
                .eventWhere(event.getEventWhere())
                .withWho(event.getWithWho())
                .EventYear(event.getEventYear())
                .EventMonth(event.getEventMonth())
                .EventDate(event.getEventDate())
                .imageUrlList(event.getImageUrl())
                .userId(event.getEventId())
                .Eventcategories(event.getEventCategories())
                .build();
    }

    @Builder
    public EventResponseDto(Long eventId, String what, String eventWhere, String withWho, int EventYear, int EventMonth, int EventDate, Long userId, List<String> imageUrlList, List<EventCategory> eventCategories){
        this.eventId = eventId;
        this.what = what;
        this.eventWhere = eventWhere;
        this.withWho = withWho;
        this.EventYear = EventYear;
        this.EventMonth = EventMonth;
        this.EventDate = EventDate;
        this.userId = userId;
        this.imageUrlList = imageUrlList;
        this.Eventcategories = eventCategories;
    }
}

