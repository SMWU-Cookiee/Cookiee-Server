package com.cookiee.cookieeserver.login.jwt;

import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.Constant.*;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    /**
     * doFilter와 같지만 요청 당 한번만 실행되는 것이 보장됨.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        final String headerValue = request.getHeader(HEADER_AUTHORIZATION);  // Request Header에서 토큰 추출
        if (servletPath.equals("/auth/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            try {
                if (headerValue != null && headerValue.startsWith(TOKEN_PREFIX)) {  // Bearer로 시작하고 값이 있으면
                    String accessToken = headerValue.substring(TOKEN_PREFIX.length());
//                    try {
                    if (jwtService.validate(accessToken)) {  // 토큰 검증
                        Authentication authentication = getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
//                    } catch (io.jsonwebtoken.JwtException)
                }
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                jwtExceptionHandler(response, e);
            }
        }
    }

    // 권한 가져오기
    public Authentication getAuthentication(String accessToken) {
        Claims claims = jwtService.getTokenClaims(accessToken);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // jwt 예외 처리
    public void jwtExceptionHandler(HttpServletResponse response, Throwable error) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponseDto.of(HttpStatus.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                    error.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
