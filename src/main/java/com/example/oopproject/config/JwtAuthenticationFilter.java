package com.example.oopproject.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Filter for authenticating requests using JWT (JSON Web Token).
 *
 * <p>This filter extracts the JWT token from the Authorization header,
 * validates it, extracts user information and roles, and sets the
 * authentication in the Spring Security context.</p>
 *
 * <p>If the token is invalid or missing, the filter does not authenticate the user.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String jwtSecret;

    /**
     * Constructs the filter with the JWT secret key.
     *
     * @param jwtSecret the secret key used to sign and verify JWT tokens
     */
    public JwtAuthenticationFilter(@Value("${jwt.secret:defaultSecretKey123!@#}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    /**
     * Performs the filtering logic to extract and validate the JWT token,
     * then sets the Spring Security authentication if valid.
     *
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to proceed with processing
     * @throws ServletException if servlet errors occur
     * @throws IOException if I/O errors occur
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Use a more robust key generation
                SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                List<SimpleGrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(role));

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Token is invalid
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
