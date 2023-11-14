package com.cookiee.cookieeserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int collectionId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event_id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
}
