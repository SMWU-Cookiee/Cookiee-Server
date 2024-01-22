package com.cookiee.cookieeserver.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        // CorsFilter를 빈으로 등록하여 CORS 정책을 처리합니다.

        // CorsConfiguration 객체를 생성합니다.
        CorsConfiguration config = new CorsConfiguration();

        // 원하는 도메인만 허용하도록 설정
        //config.addAllowedOrigin("http://localhost:8080/swagger-ui/index.html");

        // 허용할 헤더를 모두 허용하도록 설정
        config.addAllowedHeader("*");

        // 허용할 HTTP 메소드를 모두 허용하도록 설정
        config.addAllowedMethod("*");

        // Credentials 허용 여부를 설정 (true로 설정하면 자격 증명(credentials)을 허용)
        config.setAllowCredentials(false);

        // preflight 요청 결과를 캐시하는 시간을 설정 (초 단위, 여기서는 6000초 또는 1시간)
        config.setMaxAge(6000L);

        // UrlBasedCorsConfigurationSource를 생성하고, 모든 경로에 대해 위에서 설정한 CorsConfiguration을 등록합니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // CorsFilter를 생성하여 UrlBasedCorsConfigurationSource를 설정합니다.
        return new CorsFilter(source);
    }
}

