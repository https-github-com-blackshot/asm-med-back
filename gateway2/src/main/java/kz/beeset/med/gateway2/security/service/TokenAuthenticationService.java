package kz.beeset.med.gateway2.security.service;

import kz.beeset.med.gateway2.exception.model.ServiceException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;


public interface TokenAuthenticationService {

    Authentication authenticate(HttpServletRequest request) throws ServiceException;
}
