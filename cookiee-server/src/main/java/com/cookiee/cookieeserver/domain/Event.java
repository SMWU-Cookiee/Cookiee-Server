package com.cookiee.cookieeserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
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
    private Long event_id;  // 이벤트 PK

    @Column(length = 1000)
    private String what; // 이벤트 내용

    @Column(length = 1000)
    private String where; // 이벤트 장소

    @Column(length = 1000)
    private String with_who; // 함께 한 사람

    private String when;   // 이벤트 시간
    public void setMonth(LocalDate created_at) {
        String year = Integer.toString(created_at.getYear());
        String month = Integer.toString(created_at.getMonthValue());
        String date = Integer.toString(created_at.getDayOfMonth());
        String dayofweek = created_at.getDayOfWeek().toString(); //요일
        this.when = year+month+date+dayofweek; //연-월-일-요일
    }

    @Column
    private String imageUrl; //이미지 사진

    @ManyToOne  // 다대일 단방향 관계, user 삭제되면 이벤트도 삭제
    @JoinColumn(name = "userId")
    private User user_id;  // 유저 pk (FK)

    //event : category = 1 : 다 -> 수연이쪽에 매핑해주어야 함
    @OneToMany(mappedBy = "event")
    private List<Collection> collections = new ArrayList<>();

    public Event(String what, String where, String with_who, String when,
                 String imageUrl, LocalDate created_time, LocalDate modified_time){
        this.what = what;
        this.where = where;
        this.with_who = with_who;
        this.when = when;
        this.imageUrl = imageUrl;
        this.user_id = getUser_id();
        //this.collections = Collection();

    }

}
