package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.user.domain.UserV2;

public record EventGetRequestDto(int eventYear,
                                 int eventMonth,
                                 int eventDate) {
    public Event toEntity(UserV2 userV2){
        return Event.builder()
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .build();
    }
}
