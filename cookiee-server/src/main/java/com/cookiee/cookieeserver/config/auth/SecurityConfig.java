package com.cookiee.cookieeserver.config.auth;

import com.cookiee.cookieeserver.login.jwt.JwtAuthorizationFilter;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtService jwtService;
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

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers("/", "/error")  // 에러를 안거치게?
                                .permitAll()
                                .requestMatchers("/auth/**")
                                .permitAll()
                                .anyRequest()
                                .permitAll()
                )
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtService);
    }
}
