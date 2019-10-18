package kz.beeset.med.admin.configs;

import com.google.gson.Gson;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        System.out.println("================== SecurityAuditor getCurrentAuditor() START ");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication isAuthenticated() = " + (authentication != null ? authentication.isAuthenticated() : "null"));

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

        System.out.println("================== SecurityAuditor getCurrentAuditor() " + username + "END ");
        return Optional.of(username);
    }
}
