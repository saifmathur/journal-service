package com.journal.journal_service.securityFilter;

import com.journal.journal_service.dto.CustomUserDetails;
import com.journal.journal_service.services.auth.JwtTokenService;
import com.journal.journal_service.utility.JwtAuthenticationToken;
import com.journal.journal_service.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenService jwtTokenService;  // You will need a service to parse the JWT token

    public JwtTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Remove "Bearer " prefix
        }
        return null;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = extractTokenFromRequest(request);
//
//        if (token != null && jwtTokenService.validateToken(token)) {
//            String userId = this.jwtTokenService.extractUserId(token);  // Extract user ID from token
//            // Set the userId in SecurityContext or request attributes
//            SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userId));
//        }
//
//        filterChain.doFilter(request, response);
//    }
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = extractTokenFromRequest(request);

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

    // Proceed with the filter chain
    filterChain.doFilter(request, response);
}

}
