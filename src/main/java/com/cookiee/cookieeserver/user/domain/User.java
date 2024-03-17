package com.cookiee.cookieeserver.user.domain;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.global.domain.AuthProvider;
import com.cookiee.cookieeserver.global.domain.BaseTimeEntity;
import com.cookiee.cookieeserver.global.domain.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private Long userId;  // 사용자 아이디

    @Column(nullable = false, length = 10)
    private String nickname;  // 사용자가 설정한 닉네임

    @Column(nullable = false)
    private String email;  // 이메일

    @Column(nullable = false)
    private String socialId;  // 소셜 아이디

    // 기본적으로 Enum 값은 int로 저장되기 때문에 String으로 저장하도록 하기
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // 소셜 로그인 하려면 role 필요함

    @Column(nullable = false)
    private String refreshToken;  // 리프레쉬 토큰

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider socialLoginType;  // 애플인지 구글인지

    private String socialRefreshToken;  // 애플용 리프레쉬 토큰

    @Column(length = 50)
    private String selfDescription;

    @Column(nullable = false)
    private String profileImage;  // 사용자 프로필 이미지 경로

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList<>();

    @Builder
    public User(String nickname, String email, String profileImage, String description, Role role) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.selfDescription = description;
        this.role = role;
    }

    public User update(String nickname, String profileImage, String description) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.selfDescription = description;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
