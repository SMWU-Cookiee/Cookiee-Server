package com.cookiee.cookieeserver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 모아보기 요청 response dto에 사용하기 위한 이벤트 dto
@Getter
@Setter
@NoArgsConstructor
public class EventImageURLDto {
    private Long eventId;
    private String firstImageUrl;

    @Builder
    public EventImageURLDto(Long eventId, String firstImageUrl) {
        this.eventId = eventId;
        this.firstImageUrl = firstImageUrl;
    }
}
