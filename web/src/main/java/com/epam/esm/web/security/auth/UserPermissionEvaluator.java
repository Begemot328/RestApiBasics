package com.epam.esm.web.security.auth;

import com.epam.esm.model.userdetails.Account;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UserPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        if (!(authentication.getPrincipal() instanceof Account)) {
            return false;
        }
        if (!(o instanceof Integer)) {
            return false;
        }
        return ((Account) authentication.getPrincipal()).getUser().getId() == (Integer) o;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
