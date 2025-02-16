package com.journal.journal_service.securityFilter;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.journal.journal_service.utility.GoogleAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class GoogleAuthenticationFilter extends OncePerRequestFilter {
    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;


    private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(), JacksonFactory.getDefaultInstance()
    ).setAudience(Collections.singletonList(googleClientId)).build();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String googleToken = request.getHeader("Google-Auth-Token");
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
        filterChain.doFilter(request, response);

    }
}
