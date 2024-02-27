package com.cookiee.cookieeserver.login.config;

import com.cookiee.cookieeserver.login.jwt.JwtAuthorizationFilter;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration
@Slf4j
public class SecurityConfig {
    private final JwtService jwtService;
    private final AuthenticationEntryPoint entryPoint;	// 빈으로 등록된 엔트리 포인트 주입받음. 즉, JwtAuthenticationEntryPoint 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("=== SecurityFilterChain Started ===");
        http.csrf(AbstractHttpConfigurer::disable)  // 서버에 인증정보를 저장하지 않기 때문에 csrf 보안을 사용X
                // JWT를 사용하기 때문에 세션을 사용하지 않는 설정.(아래)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)  // form 기반 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // rest api이므로 basic auth 및 csrf 보안을 사용하지 않는 설정
                .authorizeHttpRequests(
                        registry -> registry
                                // permitAll()은 권한에만 신경 쓰지 않을 뿐 똑같이 모든 필터를 거침
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .anyRequest().permitAll()
                )
                // JWT 인증을 위하여 직접 구현한 jwtAuthorizationFilter 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                // exception handler
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));
        return http.build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtService);
    }
}
