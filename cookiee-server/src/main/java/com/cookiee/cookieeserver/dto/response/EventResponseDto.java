
package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Collection;
import com.cookiee.cookieeserver.domain.Event;
import lombok.Builder;

import java.util.List;

@Builder
public record EventResponseDto(
        Long eventId,
        String what,
        String eventWhere,
        String withWho,
        int EventYear,
        int EventMonth,
        int EventDate,
        String imageUrl,
        Long userId,
        List<Collection> collections){

    public static EventResponseDto from(Event event){
        return EventResponseDto.builder()
                .eventId(event.getEvent_id())
                .what(event.getEventWhat())
                .eventWhere(event.getEventWhere())
                .withWho(event.getWithWho())
                .EventYear(event.getEventYear())
                .EventMonth(event.getEventMonth())
                .EventDate(event.getEventDate())
                .imageUrl(event.getImageUrl())
                .userId(event.getEvent_id())
                .collections(event.getCollections())
                .build();
    }
}

