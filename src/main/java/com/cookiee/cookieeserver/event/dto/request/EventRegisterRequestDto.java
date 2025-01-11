package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

//@ApiModel(value = "이벤트 등록")
public record EventRegisterRequestDto (
        @Schema(description = "이벤트 내용", nullable = false, example = "")
    String eventTitle,
    String eventWhat,
    String eventWhere,
    String withWho,
    int eventYear,
    int eventMonth,
    int eventDate,
    List<Long> categoryIds){

    public Event toEntity(User user, List<EventCategory> eventCategories, List<String> imageUrls){
        return Event.builder()
                .eventTitle(eventTitle)
                .eventWhat(eventWhat)
                .eventWhereText(eventWhere)
                .withWho(withWho)
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .user(user)
                .eventCategoryList(eventCategories)
                .imageUrl(imageUrls)
                .build();
    }


}
