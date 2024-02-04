package com.cookiee.cookieeserver.login.jwt;

import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Getter
@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    private final long accessTokenExpirationTime = 1000L * 60 * 60;  // 액세스 토큰 만료 기간: 1시간
    private final long refreshTokenExpirationTime = 1000L * 60 * 60 * 24 * 30;  // 리프레쉬 토큰 만료 기간: 30일

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    // 의존성 주입이 이루어진 후 초기화 수행 (PostConstruct) -> key 생성
    @PostConstruct
    private void _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    /**
     * 액세스 토큰 생성
     * @param userSocialId
     * @return
     */
    public String createAccessToken(String userSocialId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userSocialId)
                .claim("role", "ROLE_USER")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();  // JWT의 헤더, 클레임, 암호 등 필요한 정보를 넣고 직렬화(compact)
    }

    /**
     * 리프레쉬 토큰 생성
     * @return
     */
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 리프레쉬 토큰 유효성 검증
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public String validateRefreshToken(String accessToken, String refreshToken) {
        String handle = getUserSocialId(accessToken);
        User user = userRepository.findByUserSocialId(handle);

        if (user.getRefreshToken() == null)
            throw new RefreshTokenException(NULL_REFRESH_TOKEN);

        if (!user.getRefreshToken().equals(refreshToken))
            throw new RefreshTokenException(INVALID_REFRESH_TOKEN);

        return user.getSocialId();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (SecurityException e) {
            throw new JwtException(INVALID_TOKEN_SIGNATURE.getMessage());
        } catch (MalformedJwtException e) {
            throw new JwtException(MALFORMED_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new JwtException(EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new JwtException(UNSUPPORTED_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            throw new JwtException(INVALID_TOKEN.getMessage());
        }
    }

    /**
     * 토큰을 통해 JWT 안의 claim 가져오기
     * @param token
     * @return
     */
    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUserSocialId(String token) {
        return getTokenClaims(token).getSubject();
    }

    public PostTokenResponse reissueAccessToken() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String refreshToken = JwtHeaderUtil.getRefreshToken();

        if (refreshToken == null)
            throw new RefreshTokenException(NULL_REFRESH_TOKEN);
        else if (!validate(refreshToken))
            throw new RefreshTokenException(INVALID_REFRESH_TOKEN);

        String handle = validateRefreshToken(accessToken, refreshToken);
        String newAccessToken = createAccessToken(handle);

        return PostTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    // custom
    public Member getLoginMember() {
        final String loginMemberHandle = getLoginMemberHandle();
        return memberRepository.findByHandle(loginMemberHandle);
    }

    public String getLoginMemberHandle() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        return getHandle(accessToken);
    }
}
