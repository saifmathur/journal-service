package com.journal.journal_service.utility;

import com.journal.journal_service.config.JwtConfig;
import com.journal.journal_service.models.auth.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 3600000; // 1 hour


    public static String generateTokenForGoogle(String email) {


        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(JwtConfig.getSecretKey())
                .compact();
    }

    public static String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId()); // Add user ID
        claims.put("roles", user.getRoles());  // Add roles
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(JwtConfig.getSecretKey())
                .compact();
    }

    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public Long getUserId() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;

                Map<String, Object> claims = jwt.getClaims(); // Typically, the username is stored in the 'sub' claim
                System.out.println(claims);
                return (Long) claims.get("userId");
            } else {
                throw new Exception("Principal is not an instance of Jwt.");
            }
        } else {
            throw new UsernameNotFoundException("No Authenticated User.");
        }
    }

}
