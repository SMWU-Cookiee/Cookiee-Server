
package com.cookiee.cookieeserver.dto.response;

import com.cookiee.cookieeserver.domain.Category;
//import com.cookiee.cookieeserver.domain.Collection;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import lombok.Builder;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Builder
public record EventResponseDto(
        Long eventId,
        String what,
        String eventWhere,
        String withWho,
        int EventYear,
        int EventMonth,
        int EventDate,
        String startTime,
        String endTime,
        List<String> imageUrlList,
        List<CategoryGetResponseDto> categories){

    public static EventResponseDto from(Event event){
        return new EventResponseDto(
                event.getEventId(),
                event.getEventWhat(),
                event.getEventWhere(),
                event.getWithWho(),
                event.getEventYear(),
                event.getEventMonth(),
                event.getEventDate(),
                event.getStartTime(),
                event.getEndTime(),
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
/*    @Builder
    public EventResponseDto(Long eventId, String what, String eventWhere, String withWho, int EventYear, int EventMonth, int EventDate, Long userId, List<String> imageUrlList, List<EventCategory> eventCategories){
        this.eventId = eventId;
        this.what = what;
        this.eventWhere = eventWhere;
        this.withWho = withWho;
        this.EventYear = EventYear;
        this.EventMonth = EventMonth;
        this.EventDate = EventDate;
        this.userId = userId;
        this.imageUrlList = imageUrlList;
        this.Eventcategories = eventCategories;
    }*/


