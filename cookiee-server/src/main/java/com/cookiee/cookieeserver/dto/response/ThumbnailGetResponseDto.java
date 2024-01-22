package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.Thumbnail;
import lombok.Builder;

@Builder
public record ThumbnailGetResponseDto(
    Long thumbnailId,
    int eventYear,
    int eventMonth,
    int eventDate,
    String thumbnailUrl){

    public static ThumbnailGetResponseDto from(Thumbnail thumbnail){
        return ThumbnailGetResponseDto.builder()
                .thumbnailId(thumbnail.getThumbnailId())
                .eventYear(thumbnail.getEventYear())
                .eventMonth(thumbnail.getEventMonth())
                .eventDate(thumbnail.getEventDate())
                .thumbnailUrl(thumbnail.getThumbnailUrl().toString())
                .build();
    }
}

