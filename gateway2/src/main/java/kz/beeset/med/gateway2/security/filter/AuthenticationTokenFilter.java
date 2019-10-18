package kz.beeset.med.gateway2.security.filter;

import kz.beeset.med.gateway2.repository.SessionRepository;
import kz.beeset.med.gateway2.security.service.TokenAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationTokenFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenFilter.class);
    private final TokenAuthenticationService authenticationService;


    public AuthenticationTokenFilter(final TokenAuthenticationService authenticationService){
        this.authenticationService = authenticationService;

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
//        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            final Authentication authentication = authenticationService.authenticate(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
            SecurityContextHolder.getContext().setAuthentication(null);
//        } catch (Exception e) {
//            System.out.println("ERROR - Exception while logout - message: " + e.getMessage());
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.sendRedirect("/med/access_denied");
//        }
    }
}

