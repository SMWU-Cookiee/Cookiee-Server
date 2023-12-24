package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Collection;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record EventRegisterRequestDto (
    String eventWhat,
    String eventWhere,
    String withWho,
    int eventYear,
    int eventMonth,
    int eventDate,
    String imageUrl){
    public Event toEntity(User user, List<Collection> collection){
        return Event.builder()
                .eventWhat(eventWhat)
                .eventWhere(eventWhere)
                .withWho(withWho)
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .user(user)
                .collections(collection)
                .build();
    }


}
