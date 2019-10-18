package kz.beeset.med.admin.configs;

import kz.beeset.med.admin.constant.SecurityConstants;
import kz.beeset.med.admin.model.Session;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.model.UserAuthentication;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.admin.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControllerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionRepository sessionRepository;

//    @Autowired
//    public ControllerInterceptor(SessionRepository sessionRepository) {
//        this.sessionRepository = sessionRepository;
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod()) ||
            "DELETE".equalsIgnoreCase(request.getMethod())) {
            final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);

            System.out.println("[preHandle][" + request.getMethod() + "]" + request.getRequestURI() + "[" + token + "]");

            if (token != null) {
                Session session = this.sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);
                if (session != null) {
                    User user = session.getUser();

                    if (user != null) {
                        List<GrantedAuthority> auths = new ArrayList<>();

                        List<UserRoleOrgMap> userRoleOrgMapList = user.getUserRoleOrgMapList();
                        String selectedOrganizationId = session.getSelectedOrganizationId();
                        if (userRoleOrgMapList != null && selectedOrganizationId != null) {
                            Optional<UserRoleOrgMap> matchingObject = userRoleOrgMapList.stream()
                                    .filter(userRoleOrgMap -> selectedOrganizationId.equals(userRoleOrgMap.getOrgId()))
                                    .findFirst();
                            UserRoleOrgMap currentOrgRoleMap = matchingObject.orElse(null);

                            for (String role : currentOrgRoleMap.getRoles()) {
                                auths.add(new SimpleGrantedAuthority(role.toUpperCase()));
                            }
                        }

                        UserAuthentication authentication = new UserAuthentication(new org.springframework.security.core.userdetails.User(user.getId(), "", auths), token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        return true;
    }
}
