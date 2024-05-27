package com.cookiee.cookieeserver.thumbnail.domain;

import com.cookiee.cookieeserver.global.domain.BaseTimeEntity;
import com.cookiee.cookieeserver.user.domain.UserV2;
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
    private UserV2 userV2;  // 유저 pk (FK)

    @Builder
    public Thumbnail(String thumbnailUrl, int eventYear, int eventMonth, int eventDate, UserV2 userV2){
        this.thumbnailUrl = thumbnailUrl;
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDate = eventDate;
        this.userV2 = userV2;
    }

    public void update(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

}
