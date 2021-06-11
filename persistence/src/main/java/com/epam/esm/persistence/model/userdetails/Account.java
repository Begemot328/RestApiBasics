package com.epam.esm.persistence.model.userdetails;

import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.model.userdetails.roles.SecurityRoleMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link UserDetails} implementation class for Spring Security.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class Account implements UserDetails {
    private User user;
    private Set<GrantedAuthority> roles = new HashSet<>();

    /**
     * Constructor.
     *
     * @param user {@link User} object to create account.
     */
    public Account(User user) {
        this.user = user;
        roles = user.getRoles().stream()
                .map(SecurityRoleMapper.toStringMapper)
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
