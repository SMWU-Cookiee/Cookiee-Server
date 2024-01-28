package com.cookiee.cookieeserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Thumbnail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thumbnailId;  // 썸네일 PK

    @Column(nullable = false)
    private String thumbnailUrl; // 썸네일사진 URL

    @Column(nullable = false)
    private int eventYear;

    @Column(nullable = false)
    private int eventMonth;

    @Column(nullable = false)
    private int eventDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 단방향 관계
    @JoinColumn(name = "user_id")
    private User user;  // 유저 pk (FK)

    @Builder
    public Thumbnail(String thumbnailUrl, int eventYear, int eventMonth, int eventDate, User user){
        this.thumbnailUrl = thumbnailUrl;
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDate = eventDate;
        this.user = user;
    }

    public void update(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

}
