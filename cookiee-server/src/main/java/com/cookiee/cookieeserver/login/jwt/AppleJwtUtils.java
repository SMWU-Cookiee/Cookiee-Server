package com.cookiee.cookieeserver.login.jwt;

import com.cookiee.cookieeserver.login.controller.apple.AppleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleJwtUtils {
    private final AppleClient appleClient;

    // 2. 위에서 작성된 Client를 이용해 public key 구성요소를 조회한 뒤 JWT의 서명을 검증한 후 Claim을 응답하는 메소드
//    public Claims getClaimsBy(String identityToken){
//        try {
//            // 1. public key 조회
//            ApplePublicKeyResponse response = appleClient.getAppleAuthPublicKey();
//
//            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
//            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
//            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
//                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));
//
//            // 응답받은 n, e 값은 base64 url-safe로 인코딩 되어 있기 때문에 반드시 디코딩하고나서 public key로 만들어야 한다.
//            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
//            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());
//
//            BigInteger n = new BigInteger(1, nBytes);
//            BigInteger e = new BigInteger(1, eBytes);
//
//            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
//            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
//            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
//
//            // 이후 성공적으로 서명이 검증됐다면 Identity token의 payload 들이 신뢰할 수 있는 값들이라는 증명이 완료된것이다.
//            //인코딩 되어있는 payload를 디코딩하여 apple 고유 계정 id 등 중요 요소를 획득해 사용하면된다.
//            // 그리고 필요에 따라 iss, aud 등 나머지 값들을 추가적으로 검증하면 된다.
//            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
//
//        } catch (NoSuchAlgorithmException e) {
//
//        } catch (InvalidKeySpecException e) {
//
//        } catch ( MalformedJwtException e) {
//            //토큰 서명 검증 or 구조 문제 (Invalid token)
//            new IllegalAccessException("유효하지 않은 토큰입니다.");
//        } catch (ExpiredJwtException e) {
//            new IllegalAccessException("만료된 토큰입니다.");
//        } catch (Exception e) {
//
//        }
//    }
}
