package com.cookiee.cookieeserver.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Data
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity{
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private int categoryId;  // 카테고리 아이디

    @Column(nullable = false, length = 10)  // 최대 10자
    private String categoryName;  // 카테고리명

    @Column(nullable = false, length = 10)  // 최대 10자
    private String categoryColor;  // 카테고리 색 (RGB 코드)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Category(int categoryId, String categoryName, String categoryColor){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }
}
