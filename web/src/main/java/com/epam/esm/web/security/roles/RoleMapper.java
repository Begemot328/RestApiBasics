package com.epam.esm.web.security.roles;

import com.epam.esm.model.entity.Role;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class RoleMapper {
    private static final String PREFIX = "ROLE_";

     public static final Function<String, Role> toRoleMapper = new Function<String, Role>() {
        @Override
        public Role apply(String s) {
            return Role.valueOf(s.replace(PREFIX, StringUtils.EMPTY));
        }
    };

    public static final Function<Role, String> toStringMapper = new Function<Role, String>() {
        @Override
        public String apply(Role role) {
            return PREFIX + role.getValue();
        }
    };

}
