package com.example.oopproject.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JSON Web Token (JWT) operations such as
 * generating tokens, extracting information, and validating tokens.
 * <p>
 * This class is used in authentication workflows to securely issue and validate tokens
 * based on a secret key.
 */
@Component
public class JwtUtil {

    /** Secret key used for signing and verifying JWT tokens. */
    private String secret = "secretKey";

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the username for which the token is generated
     * @return a JWT token as a String
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token to parse
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token by checking whether the username inside the token
     * matches the provided {@link UserDetails} object's username.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to compare with
     * @return true if the token is valid; false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }
}
