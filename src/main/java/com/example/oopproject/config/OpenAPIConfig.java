package com.example.oopproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the OpenAPI (Swagger) documentation.
 *
 * <p>Defines the API metadata such as title, description, version, contact information, and license.</p>
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Creates and configures the OpenAPI bean with custom API information.
     *
     * @return an OpenAPI object containing metadata for the Exercise API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exercise API")
                        .description("API for managing exercises")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
