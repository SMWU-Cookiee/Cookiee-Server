package com.cookiee.cookieeserver.login.jwt;

import com.cookiee.cookieeserver.login.CustomHttpServletRequestWrapper;
import com.cookiee.cookieeserver.login.CustomHttpServletResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.Constant.*;

@RequiredArgsConstructor
@Slf4j
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
        log.info("===doFilterInternal Started===");

        String servletPath = request.getServletPath();
        log.info("===request path: {}===", servletPath);

        // 1. Request Header에서 토큰 추출
        final String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        //HttpServletRequest newRequest = new CustomHttpServletRequestWrapper(request);
        HttpServletResponse newResponse = new CustomHttpServletResponseWrapper(response);

        if (servletPath.contains("auth")) {
            log.info("contains 'auth'");
//            filterChain.doFilter(request, response);
            // 새로운 HttpServletRequest 객체 생성
            HttpServletRequest newRequest = new HttpServletRequestWrapper(request) {
                @Override
                public String getServletPath() {
                    return "/auth/signup";  // 원하는 서블릿 경로로 수정
                }
            };

            filterChain.doFilter(newRequest, response);
        } else {
            try {
                // 2. 토큰 유효성 검사 (Bearer로 시작하고 값이 있으면)
                if (headerValue != null && headerValue.startsWith(TOKEN_PREFIX)) {
                    String accessToken = headerValue.substring(TOKEN_PREFIX.length());
                    // 정상 토큰이면 해당 토큰으로 authentication을 가져와 SecurityContext에 저장
                    if (jwtService.validate(accessToken)) {
                        log.info("[Authorization Filter] Normal JWT");
                        Authentication authentication = getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    filterChain.doFilter(request, response);
                }
            } catch (JwtException e) {
                log.debug("===Exception===");
                request.setAttribute("exception", e);
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
        log.debug("=== getting {} authentication ===", principal);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // jwt 예외 처리
    public void jwtExceptionHandler(HttpServletResponse response, Throwable error) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new HashMap<>();
        body.put("message", error.getMessage());
        body.put("isSuccess", false);
        body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
