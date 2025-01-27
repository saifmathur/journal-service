package com.journal.journal_service.dto;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


@Data
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String password, List<SimpleGrantedAuthority> authorities) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // For simplicity, assuming the account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Assuming the account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Assuming credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true;  // Assuming the user is enabled
    }
}
