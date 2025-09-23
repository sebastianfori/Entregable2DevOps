package com.devops.coffee_shop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coffee Shop API")
                        .version("1.0.0")
                        .description("API para la gestión de productos de una cafetería")
                        .contact(new Contact()
                                .name("DevOps Team")
                                .email("devops@coffeeshop.com")));
    }
}
