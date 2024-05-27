package com.cookiee.cookieeserver.thumbnail.dto.request;

import com.cookiee.cookieeserver.thumbnail.domain.Thumbnail;
import com.cookiee.cookieeserver.user.domain.UserV2;

public record ThumbnailRegisterRequestDto(
        Long thumbnailId,
        int eventYear,
        int eventMonth,
        int eventDate,
        String thumbnailUrl){

    public Thumbnail toEntity(UserV2 userV2, String thumbnailUrl){
        return Thumbnail.builder()
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .userV2(userV2)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }


}
