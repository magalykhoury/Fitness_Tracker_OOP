package com.example.oopproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration class that sets up HTTP security, CORS, session management,
 * and JWT authentication filter.
 *
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor for SecurityConfig.
     *
     * @param jwtAuthenticationFilter the JWT authentication filter to apply before the
     *                                UsernamePasswordAuthenticationFilter
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the security filter chain.
     *
     * <p>Disables CSRF, configures CORS, sets session management to stateless,
     * and configures route authorization rules. Adds JWT authentication filter
     * before the UsernamePasswordAuthenticationFilter.</p>
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // we’re a pure REST API + static UI, no classic CSRF forms
                .csrf(csrf -> csrf.disable())
                // apply CORS to all endpoints
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // JWT stateless sessions only
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // --- PUBLIC UI & STATIC ASSETS ---
                        .requestMatchers(
                                "/",                     // root (redirect to index.html)
                                "/*/.html",            // any HTML page in /static
                                "/css/**",               // CSS
                                "/js/**",                // JS
                                "/favicon.ico"
                        ).permitAll()

                        // --- JAVADOC SITE ---
                        .requestMatchers("/apidocs/**").permitAll()

                        // --- SWAGGER / OPENAPI UI ---
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // --- AUTH ENDPOINTS ---
                        .requestMatchers("/auth/*", "/api/auth/*").permitAll()

                        // everything else requires a valid JWT
                        .anyRequest().authenticated()
                )
                // hook in your JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the CORS settings for the application.
     *
     * <p>Allows all origins, HTTP methods GET, POST, PUT, PATCH, DELETE, OPTIONS,
     * and all headers. Exposes the "Authorization" header to clients.</p>
     *
     * @return the CorsConfigurationSource for CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // apply this CORS policy to all endpoints
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}