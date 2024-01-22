package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

// 모아보기 GET 요청 시 응답 DTO입니당
@Getter
@Setter
@NoArgsConstructor
public class EventCategoryGetResponseDto {
    private CategoryGetResponseDto category;
    private List<EventImageURLDto> eventImageList;

    @Builder
    public EventCategoryGetResponseDto(Category category, List<Event> eventList){
        this.category = CategoryGetResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryColor(category.getCategoryColor())
                .build();
        this.eventImageList = eventList.stream()
                .map(EventResponseDto::from)
                .map(
                        eventResponseDto -> EventImageURLDto.builder()
                                .eventId(eventResponseDto.eventId())
                                .firstImageUrl(eventResponseDto.imageUrlList().get(0))
                                .build()
                )
                .collect(Collectors.toList());
    }
}
