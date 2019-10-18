package kz.beeset.med.gateway2.controller;

import kz.beeset.med.admin.model.Organization;
import kz.beeset.med.admin.model.Session;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.model.UserAuthentication;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.gateway2.dto.LoginDTO;
import kz.beeset.med.gateway2.dto.TokenDTO;
import kz.beeset.med.gateway2.dto.UserDTO;
import kz.beeset.med.gateway2.repository.SessionRepository;
import kz.beeset.med.gateway2.repository.UsersRepository;
import kz.beeset.med.gateway2.security.service.TokenService;
import kz.beeset.med.gateway2.service.IEmailService;
import kz.beeset.med.gateway2.service.IOrganizationService;
import kz.beeset.med.gateway2.service.UsersService;
import kz.beeset.med.gateway2.util.CommonService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/med")
public class AuthenticationController extends CommonService {

    private static SecureRandom random = new SecureRandom();

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    private final TokenService tokenService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private final UsersService usersService;
    private final IOrganizationService organizationService;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IEmailService iEmailService;

    @Autowired
    public AuthenticationController(final TokenService tokenService,
                                    UsersService usersService,
                                    IOrganizationService organizationService,
                                    SessionRepository sessionRepository,
                                    BCryptPasswordEncoder bCryptPasswordEncoder,
                                    IEmailService iEmailService) {
        this.tokenService = tokenService;
        this.usersService = usersService;
        this.organizationService = organizationService;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.iEmailService = iEmailService;
    }

    @Autowired
    private UsersRepository usersRepository;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody final LoginDTO dto) throws InternalException {
        try {

//            System.out.println("username: " + dto.getUsername());
//            System.out.println("password: " + dto.getPassword());

            final TokenDTO response = tokenService.getToken(dto.getUsername(), dto.getPassword());
            LOGGER.debug("[authenticate()]: token = " + response);
            return builder(success(response));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while  authenticate  - message: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("ERROR - Error while authenticate - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.toString()));
        }
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET})
    @RequestMapping(value = "/currentUserAndToken", method = RequestMethod.GET)
    public ResponseEntity<?> getLoggedUser() throws InternalException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            UserAuthentication authentication = (UserAuthentication) auth;

            String token = authentication.getToken();
            String userIdn = auth.getName();

            Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);

            final TokenDTO response = new TokenDTO();
            response.setToken(token);
            response.setSelectedOrganizationId(session.getSelectedOrganizationId());
            response.setUser(usersService.findUserByIdn(userIdn));

            return builder(success(response));

        } catch (Exception e) {
            LOGGER.error("ERROR - Error while authenticate - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, "ERROR - Error while authenticate - message: " + e.toString()));
        }
    }

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET}, allowedHeaders = {"x-auth-token", "Content-Type"})
    @RequestMapping(value = "/currentUserOrganizations", method = RequestMethod.GET)
    public ResponseEntity<?> getLoggedUserOrganizations() throws InternalException {
        try {
            List<Organization> organizationList = new ArrayList<Organization>();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String userIdn = auth.getName();

            User user = usersService.findUserByIdn(userIdn);

            List<UserRoleOrgMap> userRoleOrgMaps = user.getUserRoleOrgMapList();

            if (!userRoleOrgMaps.isEmpty())
                for (UserRoleOrgMap userRoleOrgMap : userRoleOrgMaps) {
                    String orgId = userRoleOrgMap.getOrgId();
                    Organization organization = organizationService.getSingleOrgUnit(orgId);

                    organizationList.add(organization);
                }

            return builder(success(organizationList));
        } catch (Exception e) {
            LOGGER.error("ERROR - Error while authenticate - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.getMessage()));
        }
    }

    @RequestMapping(value = "/changeOrganization/{orgId}", method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable(name = "orgId") String orgId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            UserAuthentication authentication = (UserAuthentication) auth;

            String token = authentication.getToken();

            Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);

            session.setSelectedOrganizationId(orgId);

            sessionRepository.save(session);

            return builder(success("success"));
        } catch (Exception e) {
            LOGGER.error("ERROR - Error while authenticate - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.getMessage()));
        }
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            SecurityContextHolder.clearContext();
            return builder(success(this.tokenService.deactivateToken(request)));
        } catch (Exception e) {
            LOGGER.error("ERROR - Error while authenticate - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.getMessage()));
        }
    }

    @RequestMapping(value = "/resetPassword/{userId}/{password}", method = RequestMethod.GET)
    public ResponseEntity<?> resetPassword(@PathVariable(name = "userId") String userId, @PathVariable(name = "password") String password) {

        try {

            User user = usersService.findUserById(userId);

            if(user != null){
                user.setPassword(this.bCryptPasswordEncoder.encode(password));
                usersService.update(user);
            }else{
                return builder(success("Cannot find user with id : " + userId));
            }

            return builder(success("Password changed successfully"));

        } catch (Exception e) {
            LOGGER.error("ERROR - Error while changing password - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.getMessage()));
        }

    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<?> resetPasswordWithToken(@PathVariable(value = "token") String token, @RequestBody String password) {
        try {
            User signUpUser = usersService.findBySignupToken(token);
            if (signUpUser == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_TOKEN, "Неверный токен."));
            }
            signUpUser.setPassword(bCryptPasswordEncoder.encode(password));
            signUpUser.setSignupToken("");
            return builder(success(usersService.update(signUpUser)));
        } catch (InternalException e) {
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/forgotPassword/{idnOrMobilePhoneNumber}", method = RequestMethod.GET)
    public ResponseEntity<?> forgotPassword(@PathVariable(name = "idnOrMobilePhoneNumber") String idnOrMobilePhoneNumber) {
        try {

            User user = usersService.findUserByIdnOrMobilePhone(idnOrMobilePhoneNumber);

            if (user == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Email не найден"));
            }

            UserDTO dto = new UserDTO();
            dto.setEmail(user.getEmail());
            dto.setIdn(user.getIdn());
            dto.setMiddlename(user.getMiddlename());
            dto.setName(user.getName());
            dto.setPassword(user.getPassword());
            dto.setSurname(user.getSurname());
            dto.setSignupToken(usersService.sendMailResetPassword(dto));
            user.setSignupToken(dto.getSignupToken());
            usersRepository.save(user);
            return  builder(success("Email was send"));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ERROR - Error while forgot password - message: " + e);
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.toString()));
        }

    }

    public static String generatePassword(int len, String dic) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

}
