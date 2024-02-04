package com.cookiee.cookieeserver.Config.auth;

import com.cookiee.cookieeserver.CookieeServerApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

// FeignClient을 스프링 빈으로 등록하여 사용하기 위해 @Configuration 으로 싱글톤으로 등록
// 주의할 점은 basePackageClasses를 지정해주지 않으면 feignClient를 사용할 수 없다는 점!!
@Configuration
@EnableFeignClients(basePackageClasses = CookieeServerApplication.class)
public class FeignClientConfig {
}