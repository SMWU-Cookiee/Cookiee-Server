package com.cookiee.cookieeserver.event.domain;

import com.cookiee.cookieeserver.global.domain.BaseTimeEntity;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.global.domain.EventWhereType;
import com.cookiee.cookieeserver.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.cookiee.cookieeserver.global.domain.EventWhereType.PLACE;
import static com.cookiee.cookieeserver.global.domain.EventWhereType.TEXT;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false, length = 20)
    private String eventTitle;

    @Column(nullable = true, length = 100)
    private String eventWhat;

    @Column(nullable = true, length = 100)
    @Enumerated(EnumType.STRING)
    private EventWhereType eventWhereType;

    @Column(nullable = true, length = 100)
    private String eventWhereText;

    @ManyToOne
    @JoinColumn(name = "placeId", nullable = true)
    private Place eventWherePlace;

    @Column(nullable = true, length = 100)
    private String withWho;

    @Column(nullable = false)
    private int eventYear;

    @Column(nullable = false)
    private int eventMonth;

    @Column(nullable = false)
    private int eventDate;

    @ElementCollection
    @Column(nullable = true)
    private List<String> imageUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "event")
    private List<EventCategory> eventCategories = new ArrayList<>();

    @Builder
    public Event(String eventTitle, String eventWhat, String eventWhereText, Place eventWherePlace, String withWho, int eventYear, int eventMonth, int eventDate,
                 List<String> imageUrl, User user, List<EventCategory> eventCategoryList){
        this.eventTitle = eventTitle;
        this.eventWhat = eventWhat;
        this.eventWhereType = eventWhereText == null ? PLACE : TEXT;
        this.eventWhereText = eventWhereText;
        this.eventWherePlace = eventWherePlace;
        this.withWho = withWho;
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDate = eventDate;
        this.imageUrl = imageUrl;
        this.user = user;
        this.eventCategories = eventCategoryList;
    }

    public void update(String eventTitle, String eventWhat, String eventWhereText, Place eventWherePlace, String withWho, List<String> imageUrl, List<EventCategory> eventCategoryList){
        this.eventTitle = eventTitle;
        this.eventWhat = eventWhat;
        this.eventWhereType = eventWhereText == null ? PLACE : TEXT;
        this.eventWhereText = eventWhereText;
        this.eventWherePlace = eventWherePlace;
        this.withWho = withWho;
        this.imageUrl = imageUrl;
        this.eventCategories = eventCategoryList;
    }

}
