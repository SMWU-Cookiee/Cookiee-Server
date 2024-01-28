package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Thumbnail;
import lombok.Builder;

@Builder
public record ThumbnailResponseDto(
    Long thumbnailId,
    int eventYear,
    int eventMonth,
    int eventDate,
    String thumbnailUrl){

    public static ThumbnailResponseDto from(Thumbnail thumbnail){
        return ThumbnailResponseDto.builder()
                .thumbnailId(thumbnail.getThumbnailId())
                .eventYear(thumbnail.getEventYear())
                .eventMonth(thumbnail.getEventMonth())
                .eventDate(thumbnail.getEventDate())
                .thumbnailUrl(thumbnail.getThumbnailUrl().toString())
                .build();
    }

    @Builder
    public ThumbnailResponseDto(Long thumbnailId, int eventYear, int eventMonth, int eventDate, String thumbnailUrl){
        this.thumbnailId = thumbnailId;
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDate = eventDate;
        this.thumbnailUrl = thumbnailUrl;
    }
}

