package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Thumbnail;
import lombok.Builder;

public class ThumbnailUpdateResponseDto{
        Long thumbnailId;
        int eventYear;
        int eventMonth;
        int eventDate;
        String thumbnailUrl;

    @Builder
    public ThumbnailUpdateResponseDto(Long thumbnailId, String thumbnailUrl){
        this.thumbnailId = thumbnailId;
        this.thumbnailUrl = thumbnailUrl;
    }
}
