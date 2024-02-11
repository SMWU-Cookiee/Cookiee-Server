package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.user.domain.User;

public record EventGetRequestDto(int eventYear,
                                 int eventMonth,
                                 int eventDate) {
    public Event toEntity(User user){
        return Event.builder()
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .build();
    }
}
