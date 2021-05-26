package com.epam.esm.web.security.userdetails;

import com.epam.esm.model.entity.User;
import com.epam.esm.web.security.roles.RoleMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Account implements UserDetails {
    private User user;
    private Set<GrantedAuthority> roles = new HashSet<>();

    public Account(User user) {
        this.user = user;
        roles = user.getRoles().stream()
                .map(RoleMapper.toStringMapper)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
