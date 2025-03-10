package com.journal.journal_service.utility;

import com.journal.journal_service.constants.AppConstants;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class GoogleAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;

    public GoogleAuthenticationToken(String email) {
        super(Collections.singletonList(new SimpleGrantedAuthority(AppConstants.ROLE_GOOGLE_USER)));
        this.email = email;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }
}
