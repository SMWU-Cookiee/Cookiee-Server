package com.cookiee.cookieeserver.config;



import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> openApi.info(apiInfo("Cookiee- API"));
    }

    @Bean
    public OpenApiCustomizer userOpenApiCustomizer() {
        return openApi -> openApi.info(apiInfo("cookiee-service"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("SERVICE")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(customOpenApiCustomizer())
                .addOpenApiCustomizer(userOpenApiCustomizer())
                .build();
    }

    private Info apiInfo(String title) {
        return new Info()
                .title(title)
                .description("포토 캘린더 서비스, Cookiee-")
                .version("1.0.0");
    }
}
