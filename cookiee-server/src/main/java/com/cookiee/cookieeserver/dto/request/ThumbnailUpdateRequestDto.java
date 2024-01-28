package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Thumbnail;
import com.cookiee.cookieeserver.domain.User;

public record ThumbnailUpdateRequestDto(String thumbnailUrl) {
    public Thumbnail toEntity(String thumbnailUrl) {
        return Thumbnail.builder()
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
