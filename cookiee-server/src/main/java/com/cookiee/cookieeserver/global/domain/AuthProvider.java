package com.cookiee.cookieeserver.global.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    GOOGLE("google"),
    APPLE("apple");

    private final String provider;

//    AuthProvider(String provider) {
//        this.provider = provider;
//    }

    // provider 값으로 찾기
    public static AuthProvider of(String provider) {
        return Arrays.stream(values())
                .filter(value -> value.provider.equals(provider))
                .findAny()
                .orElse(null);
    }
}
