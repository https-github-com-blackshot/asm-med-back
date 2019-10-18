package kz.beeset.med.gateway2.security.service;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.beeset.med.admin.model.Session;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.gateway2.dto.TokenDTO;
import kz.beeset.med.gateway2.exception.model.ServiceException;
import kz.beeset.med.gateway2.repository.SessionRepository;
import kz.beeset.med.gateway2.security.constants.SecurityConstants;
import kz.beeset.med.gateway2.service.UsersService;
import kz.beeset.med.gateway2.util.error.InternalException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kz.beeset.med.gateway2.security.constants.SecurityConstants.TOKEN_EXPIRE_TIME;


@Service
public class JsonWebTokenService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(JsonWebTokenService.class);

    @Value("${security.token.secret.key}")
    private String tokenKey;

    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Autowired
    private BasicUserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public String deactivateToken(HttpServletRequest request) throws InternalException {
        final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        if (token != null) {
            Session session = this.sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);
            if (session != null) {
                Calendar now = Calendar.getInstance();
                session.setTokenExpireDate(df.format(now.getTime()));
                this.sessionRepository.save(session);
            }
        }
        return "Logged out successfully";
    }

    @Override
    public TokenDTO getToken(final String username, final String password) {

        if (username == null || password == null) {
            throw new ServiceException("Idn or password was not provided", this.getClass().getName());
        }

//        System.out.println("I am here");

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) userDetailsService.loadUserByUsername(username);

//        System.out.println("username: " + username);
//        System.out.println("password: " + password);
//        System.out.println("userDetails username: " + userDetails.getUsername());

        final User user = usersService.findUserByIdn(userDetails.getUsername());

//        System.out.println("user: " + user);

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            Calendar calendar = Calendar.getInstance();
            String createDate = df.format(calendar.getTime());

            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("clientType", "user");
            tokenData.put("userId", user.getId());
            tokenData.put("username", user.getIdn());
            tokenData.put("createDate", createDate);

            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setClaims(tokenData);

            String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();

            Session session = new Session();
            session.setToken(token);
            session.setUser(user);
            session.setTokenCreateDate(createDate);
            calendar.add(Calendar.MINUTE, TOKEN_EXPIRE_TIME);
            session.setTokenExpireDate(df.format(calendar.getTime()));

            // set default organization from previous session
            Session lastSession = sessionRepository.findFirstByUser_IdOrderByTokenCreateDateDesc(new ObjectId(user.getId()));
            if (lastSession != null && lastSession.getSelectedOrganizationId() != null) {
                session.setSelectedOrganizationId(lastSession.getSelectedOrganizationId());
            } else {
                List<UserRoleOrgMap> userRoleOrgMapList = user.getUserRoleOrgMapList();
                if (userRoleOrgMapList != null && !userRoleOrgMapList.isEmpty()) {
                    // set first organization as default on login
                    session.setSelectedOrganizationId(userRoleOrgMapList.get(0).getOrgId());
                }
            }

            this.sessionRepository.save(session);

            final TokenDTO tokenResponse = new TokenDTO();
            tokenResponse.setUser(user);
            tokenResponse.setToken(token);

            return tokenResponse;
        } else {
            throw new ServiceException("Token generation was failed during authentication", this.getClass().getName());
        }
    }
}