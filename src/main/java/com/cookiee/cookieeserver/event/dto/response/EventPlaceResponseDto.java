package com.cookiee.cookieeserver.event.dto.response;

import com.cookiee.cookieeserver.event.domain.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventPlaceResponseDto {
    private String latitude;
    private String longitude;
    private String name;
    private String fullAddress;

    @Builder
    public EventPlaceResponseDto(Place place) {
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.name = place.getName();
        this.fullAddress = place.getFullAddress();
    }
}
