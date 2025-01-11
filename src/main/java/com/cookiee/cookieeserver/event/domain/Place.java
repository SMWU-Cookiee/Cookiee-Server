package com.cookiee.cookieeserver.event.domain;

import com.cookiee.cookieeserver.global.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Place extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(nullable = true, length = 100)
    private String latitude;

    @Column(nullable = true, length = 100)
    private String longitude;

    @Column(nullable = true, length = 500)
    private String name;

    @Column(nullable = true, length = 500)
    private String fullAddress;

    @OneToMany(mappedBy = "eventWherePlace")
    private List<Event> events = new ArrayList<>();

    @Builder
    public Place(String latitude, String longitude, String name, String fullAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.fullAddress = fullAddress;
    }

}
