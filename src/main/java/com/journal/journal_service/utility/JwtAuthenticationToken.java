package com.journal.journal_service.utility;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String userId;

    // Constructor to initialize userId and authorities
    public JwtAuthenticationToken(String userId) {
        super(Collections.emptyList());  // Empty list for authorities (you can add roles/permissions later if needed)
        this.userId = userId;
        setAuthenticated(true);  // Set the token as authenticated
    }

    @Override
    public Object getCredentials() {
        return null;  // No credentials in this case
    }

    @Override
    public Object getPrincipal() {
        return userId;  // User ID is the principal
    }

    public String getUserId() {
        return userId;  // Return userId for reference
    }
}
