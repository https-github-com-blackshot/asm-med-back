package kz.beeset.med.admin.configs.permission;

import io.jsonwebtoken.*;
import kz.beeset.med.admin.model.*;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.admin.constant.UserConstants;
import kz.beeset.med.admin.repository.SessionRepository;
import kz.beeset.med.admin.service.IOrganizationService;
import kz.beeset.med.admin.service.IResourceService;
import kz.beeset.med.admin.service.IRightService;
import kz.beeset.med.admin.service.IRoleService;
import kz.beeset.med.admin.service.impl.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.List;

import static kz.beeset.med.admin.constant.DefaultConstant.TOKEN_KEY;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    private final UsersService usersService;
    private final IRoleService roleService;
    private final IRightService rightService;
    private final IResourceService resourceService;
    private final IOrganizationService organizationService;
    private final SessionRepository sessionRepository;

    @Autowired
    public CustomPermissionEvaluator(UsersService usersService,
                                     IRoleService roleService,
                                     IRightService rightService,
                                     IResourceService resourceService,
                                     IOrganizationService organizationService,
                                     SessionRepository sessionRepository) {
        this.usersService = usersService;
        this.roleService = roleService;
        this.rightService = rightService;
        this.resourceService = resourceService;
        this.organizationService = organizationService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {

        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

        return hasPrivilege(auth, targetDomainObject, targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {

        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, "", targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, Object targetDomainObject, String targetType, String permission) {

        boolean isAccess = false;

        String token = targetDomainObject.toString();
        LOGGER.info("token:=" + token);
        String selectedOrganizationId = UserConstants.ROOT_ORGANIZATION_ID;

        Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);

        LOGGER.info("[getSelectedOrganizationId]: " + session.getSelectedOrganizationId());

        if (session.getSelectedOrganizationId() != null) {

            selectedOrganizationId = session.getSelectedOrganizationId();

        }

        LOGGER.info("[hasPrivilege][selectedOrganizationId]: " + selectedOrganizationId);

        //if (auth.isAuthenticated()) {

            final Jws<Claims> tokenData = parseToken(token);
            LOGGER.info("tokenData:=" + tokenData.toString());

            User user = getUserFromToken(tokenData);

            List<UserRoleOrgMap> userRoleOrgMaps = user.getUserRoleOrgMapList();

            if (!userRoleOrgMaps.isEmpty())

                for (UserRoleOrgMap userRoleOrgMap : userRoleOrgMaps) {

                    if (userRoleOrgMap.getOrgId().equals(selectedOrganizationId) && user.getId().equals(userRoleOrgMap.getUserId())) {

                        if (!userRoleOrgMap.getRoles().isEmpty())

                            for (String roleId : userRoleOrgMap.getRoles()) {

                                try {
                                    Role role = roleService.findRoleById(roleId);

                                    if (!role.getRights().isEmpty())
                                        for (String rightId : role.getRights()) {

                                            Right right = rightService.getRightById(rightId);

                                            if (!right.getResources().isEmpty())
                                                for (String resourceId : right.getResources()) {

                                                    Resource resource = resourceService.getResourceById(resourceId);

                                                    if (resource.getResource().toLowerCase().equals(permission.toLowerCase())) {

                                                        isAccess = true;

                                                        break;

                                                    }
                                                }

                                        }

                                } catch (Exception e) {
                                    LOGGER.error(e.getMessage());
                                    return isAccess;
                                }
                            }
                    }
                }
        //}

        return isAccess;
    }

    private Jws<Claims> parseToken(final String token) {
        if (token != null) {
            try {
                LOGGER.info("[secretKey]: " + TOKEN_KEY);
                return Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(token);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private User getUserFromToken(final Jws<Claims> tokenData) {
        String idn = tokenData.getBody().get("username").toString();
        LOGGER.info("[userIdn]: " + idn);
        User user = this.usersService.findUserByIdn(idn);
        return user;
    }

}
