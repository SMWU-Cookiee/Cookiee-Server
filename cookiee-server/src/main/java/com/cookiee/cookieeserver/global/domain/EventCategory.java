package com.cookiee.cookieeserver.global.domain;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.event.domain.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EventCategory extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventCategoryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event")
    private Event event;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category")
    private Category category;

    @Builder
    public EventCategory(Event event, Category category) {
        this.event = event;
        this.category = category;
    }
}
