package com.cookiee.cookieeserver.login.config;

import com.cookiee.cookieeserver.config.WebConfig;
import com.cookiee.cookieeserver.login.jwt.JwtAuthorizationFilter;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtService jwtService;
    private final WebConfig webConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .headers().frameOptions().disable() //2
//                .and()
//                .authorizeRequests() //3
//                .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll()
//                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //4
//                .anyRequest().authenticated()//5
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")//6
//                .and()
//                .oauth2Login()//7
//                .userInfoEndpoint()//8
//                .userService(customOAuth2UserService);//9

        http.csrf(AbstractHttpConfigurer::disable)  // rest api이므로 basic auth 및 csrf 보안을 사용하지 않는 설정
                // JWT를 사용하기 때문에 세션을 사용하지 않는 설정.(아래)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)  // rest api이므로 basic auth 및 csrf 보안을 사용하지 않는 설정
                .authorizeHttpRequests(
                        registry -> registry
                                // / 아래의 경로, /error 아래의 경로의 API에 대해서는 모든 요청을 허가
                                .requestMatchers("/", "/error").permitAll()
                                .requestMatchers("/auth/**").permitAll()  // /auth 아래의 모든 경로에 대해 요청 허가
                                .anyRequest().permitAll()  // 이 밖에 모든 요청에 대해서 인증을 필요로 함
                )
         // JWT 인증을 위하여 직접 구현한 jwtAuthorizationFilter 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtService);
    }
}
