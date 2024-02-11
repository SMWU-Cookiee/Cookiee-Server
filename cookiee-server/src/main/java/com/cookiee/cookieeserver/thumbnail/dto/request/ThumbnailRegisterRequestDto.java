package com.cookiee.cookieeserver.thumbnail.dto.request;

import com.cookiee.cookieeserver.thumbnail.domain.Thumbnail;
import com.cookiee.cookieeserver.user.domain.User;

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
