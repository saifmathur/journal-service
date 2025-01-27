package com.journal.journal_service.services.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtTokenService {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public boolean validateToken(String token) {
        try {
            // Parse the JWT token
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);  // This will throw an exception if the token is invalid
            return true;  // If no exception is thrown, the token is valid
        } catch (ExpiredJwtException e) {
            // Token has expired
            return false;
        } catch (UnsupportedJwtException e) {
            // Unsupported JWT
            return false;
        } catch (SignatureException e) {
            // Invalid signature
            return false;
        } catch (Exception e) {
            // Any other exceptions related to parsing the token
            return false;
        }
    }



    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();  // Extract user ID from the "sub" claim
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}

