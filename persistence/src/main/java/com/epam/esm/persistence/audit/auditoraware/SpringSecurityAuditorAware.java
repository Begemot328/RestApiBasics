package com.epam.esm.persistence.audit.auditoraware;

import com.epam.esm.persistence.model.userdetails.Account;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link AuditorAware} implementation - provide current User
 * {@link UserDetails} from {@link SecurityContext}.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<Account> {

    /**
     * Get {@link Account} object from SecurityContext.
     *
     * @return {@link Optional} of {@link Account} object.
     */
    public Optional<Account> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        return  Optional.of(((Account) authentication.getPrincipal()));
    }
}