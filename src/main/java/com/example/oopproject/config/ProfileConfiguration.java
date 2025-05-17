package com.example.oopproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuration class for OpenAPI documentation setup based on active Spring profiles.
 */
@Configuration
public class ProfileConfiguration {

    /**
     * Creates an OpenAPI bean for API documentation.
     *
     * <p>This bean is active only when the "default" or "web" profile is enabled.</p>
     *
     * @return an OpenAPI instance configured with basic API information for the fitness tracker application
     */
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
