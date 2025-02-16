package com.journal.journal_service.securityFilter;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.journal.journal_service.dto.CustomUserDetails;
import com.journal.journal_service.services.auth.JwtTokenService;
import com.journal.journal_service.utility.GoogleAuthenticationToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String GOOGLE_CLIENT_ID = "755025461022-71ektdco9qu31sqi1r8mrgv7jv77a41a.apps.googleusercontent.com";
    private JwtTokenService jwtTokenService;  // You will need a service to parse the JWT token
    private GoogleAuthenticationFilter googleAuthenticationFilter;

    public JwtTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(), JacksonFactory.getDefaultInstance()
    ).setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)).build();

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Remove "Bearer " prefix
        }
        return null;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        String googleToken = request.getHeader("Google-Auth-Token");
        if (token != null && jwtTokenService.validateToken(token)) {
            // Extract user information from the token
            String username = jwtTokenService.extractUserId(token); // Assuming extractUsername() retrieves the username
            Claims claims = jwtTokenService.extractAllClaims(token);
            // Create a CustomUserDto to represent the authenticated user
            CustomUserDetails customUser = new CustomUserDetails(
                    Long.parseLong(jwtTokenService.extractUserId(token)), // Assuming extractUserId() returns a string
                    username,
                    null,  // Password is not required at this stage
                    null
            );

            // Set authentication in the SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(customUser, null, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        if (googleToken != null) {
            try {
                GoogleIdToken idToken = verifier.verify(googleToken);
                if (idToken != null) {
                    String email = idToken.getPayload().getEmail();
                    // Set authenticated user in Security Context (customize as needed)
                    SecurityContextHolder.getContext().setAuthentication(
                            new GoogleAuthenticationToken(email)
                    );
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

}
