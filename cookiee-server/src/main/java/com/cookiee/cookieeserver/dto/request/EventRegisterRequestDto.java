package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import com.cookiee.cookieeserver.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@ApiModel(value = "이벤트 등록")
public record EventRegisterRequestDto (
        @Schema(description = "이벤트 내용", nullable = false, example = "")

    String eventWhat,
    String eventWhere,
    String withWho,
    int eventYear,
    int eventMonth,
    int eventDate,
    String startTime,
    String endTime,
    List<Long> categoryIds){

    public Event toEntity(User user, List<EventCategory> eventCategories, List<String> imageUrls){
        return Event.builder()
                .eventWhat(eventWhat)
                .eventWhere(eventWhere)
                .withWho(withWho)
                .eventYear(eventYear)
                .eventMonth(eventMonth)
                .eventDate(eventDate)
                .startTime(startTime)
                .endTime(endTime)
                .user(user)
                .eventCategoryList(eventCategories)
                .imageUrl(imageUrls)
                .build();
    }


}
