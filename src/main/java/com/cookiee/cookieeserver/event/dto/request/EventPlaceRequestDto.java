package com.cookiee.cookieeserver.event.dto.request;

import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.event.domain.Place;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.user.domain.User;

import java.util.List;

public record EventPlaceRequestDto (
    String latitude,
    String longitude,
    String name,
    String fullAddress
    ){

    public Place toEntity(){
        return Place.builder()
                .latitude(latitude)
                .longitude(longitude)
                .name(name)
                .fullAddress(fullAddress)
                .build();
    }
}
