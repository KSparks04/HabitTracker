package com.project.security;

import com.project.model.User; // Make sure this matches your User entity import
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Expose your raw User entity if you ever need it in controllers/services
    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        // If your app uses email to log in, return email here!
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return user roles/authorities here (e.g., Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}