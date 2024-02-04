package com.cookiee.cookieeserver.login.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

// apple에서 조회할 public key를 담을 response dto
@Getter
@Setter
public class ApplePublicKeyResponse {
    private List<Key> keys;

    @Getter
    @Setter
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }

    // identity Token내 payload에 속한 값들이 변조되지 않았는지 검증하기 위해서
    // 애플 서버의 public key를 사용해 JWS E256 signature를 검증하는 메소드
    public Optional<ApplePublicKeyResponse.Key> getMatchedKeyBy(String kid, String alg) {
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst();
    }
}