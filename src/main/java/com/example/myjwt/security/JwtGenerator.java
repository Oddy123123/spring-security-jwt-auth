package com.example.myjwt.security;

import com.example.myjwt.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    /**
     * Generate a JWT token
     * @param subject username or email (used by user details service to retrieve the Entity)
     * @param type takes a value from enum UserTypes
     * @param roles list of roles used for authorization
     * @return JWT token as a String (base64 encoded)
     */
    public String generateToken(String subject, String type, List<Role> roles) {
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);
        Date expireAt = new Date(nowMillis + 3600000); // 1 hour expiration

        return Jwts.builder()
                .setSubject(subject)
                .claim("type", type)
                .claim("roles", roles.stream().map(Role::getName).toList())
                .setIssuedAt(issuedAt)
                .setExpiration(expireAt)
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }

    // Check if the given JWT token is valid
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract claims (such as user type and roles) from the given JWT token
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
    }
}
