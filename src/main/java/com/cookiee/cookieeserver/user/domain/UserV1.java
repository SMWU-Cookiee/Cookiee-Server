package com.cookiee.cookieeserver.user.domain;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.global.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
public class UserV1 extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String deviceId;

    private boolean registered;

    private boolean deleted;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private UserV1(String deviceId, boolean registered, boolean deleted){
        this.deviceId = deviceId;
        this.registered = registered;
        this.deleted = deleted;
    }

    public static UserV1 registerNewUser(String deviceId){
        return UserV1.builder()
                .deviceId(deviceId)
                .registered(true)
                .build();
    }

}

