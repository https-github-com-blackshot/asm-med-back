package kz.beeset.med.device.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditor implements AuditorAware<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAuditor.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        LOGGER.info("================== SecurityAuditor getCurrentAuditor() START ");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authentication isAuthenticated() = " + (authentication != null ? authentication.isAuthenticated() : "null"));

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        LOGGER.info("================== SecurityAuditor getCurrentAuditor() " + username + "END ");
        return Optional.of(username);
    }
}
