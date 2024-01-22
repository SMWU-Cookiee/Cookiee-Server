package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

// 모아보기 GET 요청 시 응답 DTO입니당
@Getter
@NoArgsConstructor
public class EventCategoryGetResponseDto {
    private CategoryGetResponseDto category;
    private List<EventResponseDto> eventList;

    @Builder
    public EventCategoryGetResponseDto(Category category, List<Event> eventList){
        this.category = CategoryGetResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryColor(category.getCategoryColor())
                .build();
        this.eventList = eventList.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }
}
