package com.ungman.sc.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    // Swagger UI 접근: http://localhost:80/swagger-ui/index.html
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(components())
                .info(apiInfo());
    }

    // Swagger 문서 기본 정보 설정
    private Info apiInfo() {
        return new Info()
                .title("Soldesk Community API")
                .description("SC API reference for developers")
                .version("1.0");
    }

    // 보안 스키마 설정
    private Components components() {
        return new Components().addSecuritySchemes("BearerAuth",
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }
}
