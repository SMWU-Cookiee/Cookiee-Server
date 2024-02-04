package com.cookiee.cookieeserver.login.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        if (servletPath.equals("/api/v1/user/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            try {
                if (headerValue != null && headerValue.startsWith(TOKEN_PREFIX)) {
                    String accessToken = headerValue.substring(TOKEN_PREFIX.length());
                    if (jwtService.validate(accessToken)) {
                        Authentication authentication = getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                jwtExceptionHandler(response, e);
            }
        }
    }
}
