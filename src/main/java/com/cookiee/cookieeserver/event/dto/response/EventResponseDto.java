
package com.cookiee.cookieeserver.event.dto.response;

import com.cookiee.cookieeserver.category.dto.response.CategoryGetResponseDto;
import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import lombok.Builder;

import java.util.List;
        import java.util.stream.Collectors;

@Builder
public record EventResponseDto(
        Long eventId,
        String title,
        String what,
        String eventWhere,
        String withWho,
        int EventYear,
        int EventMonth,
        int EventDate,
        List<String> eventImageUrlList,
        List<CategoryGetResponseDto> categories){

    public static EventResponseDto from(Event event){
        return new EventResponseDto(
                event.getEventId(),
                event.getEventTitle(),
                event.getEventWhat(),
                event.getEventWhereText(),
                event.getWithWho(),
                event.getEventYear(),
                event.getEventMonth(),
                event.getEventDate(),
                event.getImageUrl(),
                event.getEventCategories().stream()
                        .map(EventCategory::getCategory)
                        .map(category ->
                            CategoryGetResponseDto.builder()
                                    .categoryId(category.getCategoryId())
                                    .categoryName(category.getCategoryName())
                                    .categoryColor(category.getCategoryColor())
                                    .build()
                        )
                        .collect(Collectors.toList())
        );
    }
}


