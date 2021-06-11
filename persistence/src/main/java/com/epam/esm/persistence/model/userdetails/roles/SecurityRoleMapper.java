package com.epam.esm.persistence.model.userdetails.roles;

import com.epam.esm.persistence.model.entity.Role;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * {@link SecurityRoles} to and from {@link String} mapping class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class SecurityRoleMapper {
    private static final String PREFIX = "ROLE_";

    public static final Function<String, Role> toRoleMapper = new Function<String, Role>() {
        @Override
        public Role apply(String s) {
            return new Role(s.replace(PREFIX, StringUtils.EMPTY));
        }
    };

    public static final Function<Role, String> toStringMapper = new Function<Role, String>() {
        @Override
        public String apply(Role role) {
            return PREFIX + role.getName();
        }
    };
}
