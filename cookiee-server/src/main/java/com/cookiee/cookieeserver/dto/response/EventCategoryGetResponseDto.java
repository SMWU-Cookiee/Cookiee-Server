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
    private CategoryGetResponseDto categoryGetResponseDto;
    private List<EventResponseDto> eventResponseDtoList;

    @Builder
    public EventCategoryGetResponseDto(Category category, List<Event> eventList){
        this.categoryGetResponseDto = CategoryGetResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryColor(category.getCategoryColor())
                .build();
        this.eventResponseDtoList = eventList.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }
}
