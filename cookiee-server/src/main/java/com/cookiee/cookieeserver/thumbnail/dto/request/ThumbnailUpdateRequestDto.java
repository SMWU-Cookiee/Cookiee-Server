package com.cookiee.cookieeserver.thumbnail.dto.request;

import com.cookiee.cookieeserver.thumbnail.domain.Thumbnail;

public record ThumbnailUpdateRequestDto(String thumbnailUrl) {
    public Thumbnail toEntity(String thumbnailUrl) {
        return Thumbnail.builder()
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
