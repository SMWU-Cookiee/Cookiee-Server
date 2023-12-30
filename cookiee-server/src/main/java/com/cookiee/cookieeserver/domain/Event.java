package com.cookiee.cookieeserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;  // 이벤트 PK

    @Column(nullable = true, length = 100)
    private String eventWhat; // 이벤트 내용

    @Column(nullable = true, length = 100)
    private String eventWhere; // 이벤트 장소

    @Column(nullable = true, length = 100)
    private String withWho; // 함께 한 사람

    @Column(nullable = false)
    private int eventYear;

    @Column(nullable = false)
    private int eventMonth;

    @Column(nullable = false)
    private int eventDate;

    @Column(nullable = true)
    private List<String> imageUrl; //이미지 사진

    @ManyToOne  // 다대일 단방향 관계, user 삭제되면 이벤트도 삭제
    @JoinColumn(name = "userId")
    private User user;  // 유저 pk (FK)

    //event : category = 1 : 다 -> 수연이쪽에 매핑해주어야 함
    @OneToMany(mappedBy = "event")
    private List<Category> categories = new ArrayList<>();

    @Builder
    public Event(String eventWhat, String eventWhere, String withWho, int eventYear, int eventMonth, int eventDate,
                 List<String> imageUrl, User user, List<Category> categories){
        this.eventWhat = eventWhat;
        this.eventWhere = eventWhere;
        this.withWho = withWho;
        this.eventYear = eventYear;
        this.eventMonth = eventMonth;
        this.eventDate = eventDate;
        this.imageUrl = imageUrl;
        this.user = user;
        this.categories = categories;


    }

}
