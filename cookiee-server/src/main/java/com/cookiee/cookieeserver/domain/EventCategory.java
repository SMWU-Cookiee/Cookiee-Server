package com.cookiee.cookieeserver.domain;

import com.cookiee.cookieeserver.dto.response.EventCategoryGetResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EventCategory extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category")
    private Category category;

    @Builder
    public EventCategory(Event event, Category category) {
        this.event = event;
        this.category = category;
    }
}
