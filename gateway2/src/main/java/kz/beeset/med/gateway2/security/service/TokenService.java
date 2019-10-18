package kz.beeset.med.gateway2.security.service;

import kz.beeset.med.gateway2.dto.TokenDTO;
import kz.beeset.med.gateway2.util.error.InternalException;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    String deactivateToken(HttpServletRequest request) throws InternalException;
    TokenDTO getToken(String username, String password) throws InternalException;
}
