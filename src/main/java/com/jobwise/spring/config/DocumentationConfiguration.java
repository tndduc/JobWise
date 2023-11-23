package com.jobwise.spring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "JobWise Spring Boot RestFull API",
        description = "JobWise Spring Boot RestFull API <3.",
        version = "v1.0.0"
))
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class DocumentationConfiguration {
}