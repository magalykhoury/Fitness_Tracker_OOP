package com.example.oopproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class ProfileConfiguration {

    @Bean
    @Profile({"default", "web"})
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fitness Tracker API")
                        .description("API for fitness tracking application")
                        .version("1.0.0"));
    }
}