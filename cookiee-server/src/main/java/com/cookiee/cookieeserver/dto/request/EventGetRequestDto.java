package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import com.cookiee.cookieeserver.domain.User;

import java.util.List;

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
