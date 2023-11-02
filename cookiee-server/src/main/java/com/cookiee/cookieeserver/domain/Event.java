package com.cookiee.cookieeserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.experimental.categories.Category;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Event {

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

    private LocalDate created_time;   // 생성 시간
    @PrePersist // DB에 해당 테이블의 insert 연산을 실행할 때 같이 실행해라
    public void created_at(){
        this.created_time = LocalDate.now();
        setMonth(created_time);
    }

    private LocalDate modified_time; // 수정 시간
    @PreUpdate // DB에 해당 테이블의 update 연산을 실행할 때 같이 실행해라
    public void modified_at() {
        this.modified_time = LocalDate.now();
        setMonth(modified_time);
    }

    @ManyToOne  // 다대일 단방향 관계, user 삭제되면 이벤트도 삭제
    @JoinColumn(name = "userId")
    private User user_id;  // 유저 pk (FK)

    //event : category = 1 : 다 -> 수연이쪽에 매핑해주어야 함
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    private List<Category> categories;

    public Event(String what, String where, String with_who, String when,
                 String imageUrl, LocalDate created_time, LocalDate modified_time){
        this.what = what;
        this.where = where;
        this.with_who = with_who;
        this.when = when;
        this.imageUrl = imageUrl;
        this.created_time = created_time;
        this.modified_time = modified_time;
        this.user_id = getUser_id();
        this.categories = getCategories();

    }

}
