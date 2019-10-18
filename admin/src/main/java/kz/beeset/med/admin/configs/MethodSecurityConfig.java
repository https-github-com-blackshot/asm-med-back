package kz.beeset.med.admin.configs;

import kz.beeset.med.admin.configs.permission.CustomPermissionEvaluator;
import kz.beeset.med.admin.repository.SessionRepository;
import kz.beeset.med.admin.service.IOrganizationService;
import kz.beeset.med.admin.service.IResourceService;
import kz.beeset.med.admin.service.IRightService;
import kz.beeset.med.admin.service.IRoleService;
import kz.beeset.med.admin.service.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final UsersService usersService;
    private final IRoleService roleService;
    private final IRightService rightService;
    private final IResourceService resourceService;
    private final IOrganizationService organizationService;
    private final SessionRepository sessionRepository;

    @Autowired
    public MethodSecurityConfig(UsersService usersService,
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
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(this.usersService, this.roleService, this.rightService, this.resourceService, this.organizationService, this.sessionRepository));
        return expressionHandler;
    }
}
