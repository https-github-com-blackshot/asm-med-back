package kz.beeset.med.gateway2.controller;

import io.swagger.annotations.ApiOperation;
import kz.beeset.med.admin.model.Constants;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.gateway2.constant.UserConstants;
import kz.beeset.med.gateway2.converter.ConverterFacade;
import kz.beeset.med.gateway2.dto.UserDTO;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.gateway2.service.UsersService;
import kz.beeset.med.gateway2.util.CommonService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.validators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/med/signup")
public class SignUpController extends CommonService {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);
    private final UsersService usersService;
    private final ConverterFacade converterFacade;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SignUpController(final UsersService usersService,
                            final ConverterFacade converterFacade,
                            final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersService = usersService;
        this.converterFacade = converterFacade;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody final UserDTO dto) {
        try {

            if(dto.getName() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле имя не заполнено"));
            }
            if(dto.getSurname() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле фамилия не заполнено"));
            }
            if(dto.getIdn() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле ИИН не заполнено"));
            }
            if(dto.getMobilePhone() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле номер телефона не заполнено"));
            }
            if(dto.getEmail() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле email не заполнено"));
            }
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.validate(dto.getEmail())) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Неверный e-mail формат"));
            }
            if(dto.getSex() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле пол не заполнено"));
            }
            if(dto.getBirthday() == null){
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Поле дата рождения не заполнено"));
            }
            // send to email confirmation link to applicant
            String signupToken = usersService.sendEmailConfirmLink(dto);
            dto.setSignupToken(signupToken);

            String mobilePhone = dto.getMobilePhone();

            if(!mobilePhone.isEmpty()){
                if(usersService.findUserByIdnOrMobilePhone(mobilePhone) != null){
                    return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Пользователь с таким номером телефона существует"));
                }
            }
            if(!dto.getIdn().isEmpty()){
                if(usersService.findUserByIdnOrMobilePhone(dto.getIdn()) != null){
                    return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Пользователь с таким ИИН существует"));
                }
            }
            if(!dto.getEmail().isEmpty()){
                if(usersService.findUserByIdnOrMobilePhone(dto.getEmail()) != null){
                    return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Пользователь с таким email уже существует"));
                }
            }

            User newUser = usersService.create(converterFacade.convert(dto));

            newUser.setMobilePhone(mobilePhone);
            newUser.setSignupToken(signupToken);
            newUser.setAddress(dto.getAddress());
            newUser.setSex(dto.getSex());
            newUser.setWorkplace(dto.getWorkplace());
            // To add one day [solving bug with date - 1 ]
            LocalDate modifiedBirthday = dto.getBirthday().plusDays(1);
            newUser.setBirthday(modifiedBirthday);
            //шифровать пароль
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            // Присвоение корневого организации и роль пациента
            UserRoleOrgMap userRoleOrgMap = new UserRoleOrgMap();
            userRoleOrgMap.setUserId(newUser.getId());
            userRoleOrgMap.setOrgId(UserConstants.ROOT_ORGANIZATION_ID);
            List<String> roles = new ArrayList<String>();
            roles.add(UserConstants.DEFAULT_USER_ROLE_ID);
            userRoleOrgMap.setRoles(roles);

            List<UserRoleOrgMap> userRoleOrgMaps = new ArrayList<UserRoleOrgMap>();
            userRoleOrgMaps.add(userRoleOrgMap);

            newUser.setUserRoleOrgMapList(userRoleOrgMaps);
            newUser.setNationality(dto.getNationality());
            //аудирование
            newUser.setCreatedBy("");
            newUser.setCreatedDate(LocalDateTime.now());

            //присвоение статуса
            newUser.setState(UserConstants.STATUS_ACTIVE);

            //Активность пользователя
            newUser.setActive(UserConstants.DEFAULT_IS_ACTIVE);
            newUser.setAvatarId("5d7f2d0e6a0a04043a546845");

            this.usersService.update(newUser);

            newUser.setSignupToken(null);
            newUser.setPassword(null);

            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } catch (InternalException e) {
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ERROR - Exception while sign-up - message: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.AUTH_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity<?> confirmSignUp(@PathVariable(value = "token") String token) {
        try {
            User signUpUser = usersService.findBySignupToken(token);

            if (signUpUser == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.INVALID_TOKEN, "Неверный токен."));
            }
            signUpUser.setActive(true);

            return builder(success(usersService.update(signUpUser)));
        } catch (InternalException e) {
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить цвета пользователя", tags = {"Users"})
    @RequestMapping(value = "/user/color/read", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUserColors() throws InternalException {
        try {
            return builder(success(Constants.USER_COLORS.values()));
        } catch (Exception e) {
            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, e.getMessage()));
        }
    }
}
