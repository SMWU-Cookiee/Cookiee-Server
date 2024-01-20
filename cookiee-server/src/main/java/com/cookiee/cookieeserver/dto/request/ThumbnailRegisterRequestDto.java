package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.Thumbnail;
import com.cookiee.cookieeserver.domain.User;

import java.util.List;

public record ThumbnailRegisterRequestDto(
        Long thumbnailId,
        int eventYear,
        int eventMonth,
        int eventDate,
        String thumbnailUrl){

    public Thumbnail toEntity(User user, String thumbnailUrl){
        return Thumbnail.builder()
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .user(user)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }


}
