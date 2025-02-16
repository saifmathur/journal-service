package com.journal.journal_service.utility;

import com.journal.journal_service.services.auth.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtTokenService jwtTokenService;

    public OAuth2LoginSuccessHandler(JwtTokenService jwtTokenService) {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // This will hold the OAuth2User object
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = oauthToken.getPrincipal();


        // You can extract user details, for example:
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");

        // Log or save user information, if necessary
        System.out.println("User " + name + " logged in with email: " + email);
        // Custom redirect after login
        response.sendRedirect("/entries");
    }
}
