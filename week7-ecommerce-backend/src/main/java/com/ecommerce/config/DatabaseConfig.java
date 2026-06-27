package com.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    @Bean
    OpenAPI ecommerceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("E-commerce Database API")
                .version("1.0.0")
                .description("Transactional e-commerce backend using Spring Data JPA and PostgreSQL"));
    }
}
