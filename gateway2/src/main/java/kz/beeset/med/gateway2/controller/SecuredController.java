package kz.beeset.med.gateway2.controller;

import kz.beeset.med.admin.model.Organization;
import kz.beeset.med.admin.model.Session;
import kz.beeset.med.admin.model.UserAuthentication;
import kz.beeset.med.gateway2.config.permission.CustomPermissionEvaluator;
import kz.beeset.med.gateway2.constant.UserConstants;
import kz.beeset.med.gateway2.dto.LoginDTO;
import kz.beeset.med.gateway2.repository.SessionRepository;
import kz.beeset.med.gateway2.service.IOrganizationService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import kz.beeset.med.gateway2.util.CommonService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import org.springframework.security.core.Authentication;

@RestController
public class SecuredController extends CommonService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuredController.class);

    @Autowired
    IOrganizationService organizationService;

    @Autowired
    SessionRepository sessionRepository;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<?> sayHello() {

        return new ResponseEntity<>("Secured hello!", HttpStatus.OK);
    }

    @RequestMapping(value = "/super_admin", method = RequestMethod.GET)
    public ResponseEntity<?> saySuperAdmin() {
        return new ResponseEntity<>("Secured super admin!", HttpStatus.OK);
    }

    @PostAuthorize("hasPermission(#orgId, '/users/read')")
    @RequestMapping(method = RequestMethod.GET, value = "/foos/{orgId}")
    @ResponseBody
    public Foo findById(@PathVariable String orgId) {
        return new Foo("Sample");
    }

    @PreAuthorize("hasPermission(#orgId, 'write')")
    @RequestMapping(value = "/foos", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody final LoginDTO dto, @RequestParam String orgId) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/med/access_denied", method = RequestMethod.GET)
    public ResponseEntity<?> accessDenied() {
        return builder(errorWithDescription(ErrorCode.ErrorCodes.NOT_AUTHORIZED, "Access denied"));
    }

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
