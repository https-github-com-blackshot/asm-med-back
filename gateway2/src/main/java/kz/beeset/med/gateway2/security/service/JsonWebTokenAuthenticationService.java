package kz.beeset.med.gateway2.security.service;

import io.jsonwebtoken.*;
import kz.beeset.med.admin.model.Session;
import kz.beeset.med.gateway2.exception.model.ServiceException;
import kz.beeset.med.gateway2.exception.model.UserNotFoundException;
import kz.beeset.med.admin.model.UserAuthentication;
import kz.beeset.med.gateway2.repository.SessionRepository;
import kz.beeset.med.gateway2.security.constants.SecurityConstants;
import kz.beeset.med.gateway2.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static kz.beeset.med.gateway2.security.constants.SecurityConstants.TOKEN_EXPIRE_TIME;

@Service
public class JsonWebTokenAuthenticationService implements TokenAuthenticationService {

    @Value("${security.token.secret.key}")
    private String secretKey;

    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public Authentication authenticate(final HttpServletRequest request) {

        final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        final Jws<Claims> tokenData = parseToken(token);

        if (tokenData != null) {
            User user = getUserFromToken(tokenData);
            if (user != null) {
                if (!isTokenExpired(token)) {
                    return new UserAuthentication(user, this.usersService, token);
                } else {
                    throw new ServiceException("Token was expired", this.getClass().getName());
                }
            }
        }

        return null;
    }

    private Jws<Claims> parseToken(final String token) {
        if (token != null) {
            try {
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private boolean isTokenExpired(String token) {
        try {
            Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);

            if (session != null) {

                Date tokenExpireDate = df.parse(session.getTokenExpireDate());

                Calendar now = Calendar.getInstance();
                Calendar tokenExpirationDateCalendar = Calendar.getInstance();
                tokenExpirationDateCalendar.setTime(tokenExpireDate);

                boolean isTokenExpired = tokenExpirationDateCalendar.before(now);

                if (!isTokenExpired) {
                    now.add(Calendar.MINUTE, TOKEN_EXPIRE_TIME);

                    session.setTokenExpireDate(df.format(now.getTime()));
                    sessionRepository.save(session);
                }

                return isTokenExpired;

            } else {
                throw new ServiceException("Session does not exist", this.getClass().getName());
            }
        } catch (ParseException e) {
            throw new ServiceException("Error during token parsing and verifying");
        }
    }
    private User getUserFromToken(final Jws<Claims> tokenData) {
        try {
            return (User) userDetailsService.loadUserByUsername(tokenData.getBody().get("username").toString());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User" + tokenData.getBody().get("username").toString() + " not found");
        }
    }

}