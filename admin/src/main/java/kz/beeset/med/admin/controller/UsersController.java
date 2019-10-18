package kz.beeset.med.admin.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.*;
import kz.beeset.med.admin.constant.UserConstants;
import kz.beeset.med.admin.model.Role;
import kz.beeset.med.admin.model.Session;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.model.common.UserOrgRoleUnit;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.admin.repository.SessionRepository;
import kz.beeset.med.admin.service.impl.RoleService;
import kz.beeset.med.admin.service.impl.UsersService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import static kz.beeset.med.admin.constant.SecurityConstants.AUTH_HEADER_NAME;

@RestController
@RequestMapping("/users")
@Api(tags = {"Users"}, description = "Управление пользователями", authorizations = {@Authorization(value = "bearerAuth")})
public class UsersController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    private UsersService usersService;

    private SessionRepository sessionRepository;

    private RoleService roleService;

    @Autowired
    public UsersController(UsersService usersService, SessionRepository sessionRepository, RoleService roleService) {
        this.usersService = usersService;
        this.sessionRepository = sessionRepository;
        this.roleService = roleService;
    }

    @ApiOperation(value = "Получить список пользователей", tags = {"Users"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Только возвращает пользователя с данным id", paramType = "query"),
            @ApiImplicitParam(name = "healthStatus", dataType = "int", value = "Только возвращает пользователя с данным статусом здоровья", allowableValues = "true,false", paramType = "query"),
            @ApiImplicitParam(name = "isActive", dataType = "boolean", value = "Только возвращает пользователя с данным isActive", allowableValues = "true,false", paramType = "query"),
            @ApiImplicitParam(name = "idn", dataType = "string", value = "Только возвращает пользователей с данным idn", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Только возвращает пользователей с данным name", paramType = "query"),
            @ApiImplicitParam(name = "middlename", dataType = "string", value = "Только возвращает пользователей с данным middlename", paramType = "query"),
            @ApiImplicitParam(name = "surname", dataType = "string", value = "Только возвращает пользователей с данным surname", paramType = "query"),
            @ApiImplicitParam(name = "email", dataType = "string", value = "Только возвращает пользователей с данным email", paramType = "query"),
            @ApiImplicitParam(name = "sex", dataType = "string", value = "Только возвращает пользователей с данным sex", paramType = "query"),
            @ApiImplicitParam(name = "accessMode", dataType = "string", value = "Только возвращает пользователей с данным accessMode", paramType = "query"),
            @ApiImplicitParam(name = "mobilePhone", dataType = "string", value = "Только возвращает пользователей с данным mobilePhone", paramType = "query"),
            @ApiImplicitParam(name = "contactPhone", dataType = "string", value = "Только возвращает пользователей с данным contactPhone", paramType = "query"),
            @ApiImplicitParam(name = "idnLike", dataType = "string", value = "Только возвращает пользователей с таким idn, таким как заданное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "nameLike", dataType = "string", value = "Только возвращает пользователей с таким name, таким как заданное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "surnameLike", dataType = "string", value = "Только возвращает пользователей с таким surname, как данное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "middlenameLike", dataType = "string", value = "Только возвращает пользователей с таким middlename, как данное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "emailLike", dataType = "string", value = "Только возвращает пользователей с таким email, как данное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "memberOfOrganizationWithId", dataType = "string", value = "Только возвращает пользователей, которые являются членами данной организации.", paramType = "query"),
            @ApiImplicitParam(name = "memberOfDoctorWithId", dataType = "string", value = "Только возвращает пользователей, которые являются пациентом данного доктора.", paramType = "query"),
            @ApiImplicitParam(name = "memberOfRoleWithCode", dataType = "string", value = "Только возвращает пользователей, которые являются членами данной роли.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,idn,name,middlename,surname,email,sex", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/read')")
    @RequestMapping(value = "/read", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUsers(@ApiParam(hidden = true)
                                      @RequestParam Map<String, String> allRequestParams,
                                      @RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken)
            throws InternalException {
        try {

            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = UserConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = UserConstants.DEFAUT_PAGE_SIZE;

            String sortBy = UserConstants.ID_FIELD_NAME;

            boolean isActive = UserConstants.DEFAULT_IS_ACTIVE;

            String selectedOrganizationId = null;

            if (accessToken != null && !accessToken.isEmpty()) {

                Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(accessToken);

                if (session != null) {

                    if (session.getSelectedOrganizationId() != null) {

                        selectedOrganizationId = session.getSelectedOrganizationId();

                    }
                }
            }

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(UserConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("healthStatus")) {
                query.addCriteria(Criteria.where("healthStatus").is(allRequestParams.get("healthStatus")));
            }
            if (allRequestParams.containsKey("isActive")) {
                query.addCriteria(Criteria.where(UserConstants.ISACTIVE_FIELD_NAME).is(allRequestParams.get("isActive")));
            } else {
                //query.addCriteria(Criteria.where(UserConstants.ISACTIVE_FIELD_NAME).is(isActive));
            }
            if (allRequestParams.containsKey("idn")) {
                query.addCriteria(Criteria.where(UserConstants.IDN_FIELD_NAME).is(allRequestParams.get("idn")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(UserConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("middlename")) {
                query.addCriteria(Criteria.where(UserConstants.MIDDLENAME_FIELD_NAME).is(allRequestParams.get("middlename")));
            }
            if (allRequestParams.containsKey("surname")) {
                query.addCriteria(Criteria.where(UserConstants.SURNAME_FIELD_NAME).is(allRequestParams.get("surname")));
            }
            if (allRequestParams.containsKey("email")) {
                query.addCriteria(Criteria.where(UserConstants.EMAIL_FIELD_NAME).is(allRequestParams.get("email")));
            }
            if (allRequestParams.containsKey("sex")) {
                query.addCriteria(Criteria.where(UserConstants.SEX_FIELD_NAME).is(allRequestParams.get("sex")));
            }
            if (allRequestParams.containsKey("accessMode")) {
                query.addCriteria(Criteria.where(UserConstants.ACCESS_MODE_FIELD_NAME).is(Integer.parseInt(allRequestParams.get("accessMode"))));
            }
            if (allRequestParams.containsKey("mobilePhone")) {
                query.addCriteria(Criteria.where(UserConstants.MOBILEPHONE_FIELD_NAME).is(allRequestParams.get("mobilePhone")));
            }
            if (allRequestParams.containsKey("contactPhone")) {
                query.addCriteria(Criteria.where(UserConstants.CONTACTPHONE_FIELD_NAME).is(allRequestParams.get("contactPhone")));
            }

            if (allRequestParams.containsKey("idnLike")) {
                query.addCriteria(Criteria.where(UserConstants.IDN_FIELD_NAME).regex(allRequestParams.get("idnLike")));
            }
            if (allRequestParams.containsKey("nameLike")) {
                query.addCriteria(Criteria.where(UserConstants.NAME_FIELD_NAME).regex(allRequestParams.get("nameLike")));
            }
            if (allRequestParams.containsKey("surnameLike")) {
                query.addCriteria(Criteria.where(UserConstants.SURNAME_FIELD_NAME).regex(allRequestParams.get("surnameLike")));
            }
            if (allRequestParams.containsKey("middlenameLike")) {
                query.addCriteria(Criteria.where(UserConstants.MIDDLENAME_FIELD_NAME).regex(allRequestParams.get("middlenameLike")));
            }
            if (allRequestParams.containsKey("emailLike")) {
                query.addCriteria(Criteria.where(UserConstants.EMAIL_FIELD_NAME).regex(allRequestParams.get("emailLike")));
            }

            if (allRequestParams.containsKey("memberOfOrganizationWithId")) {
                query.addCriteria(Criteria.where(allRequestParams.get("memberOfOrganizationWithId")).all(UserConstants.ORGANIZATIONIDS_FIELD_NAME));
            }
            if (allRequestParams.containsKey("memberOfDoctorWithId")) {
                query.addCriteria(Criteria.where(allRequestParams.get("memberOfDoctorWithId")).all(UserConstants.DOCTORIDS_FIELD_NAME));
            }
            if (allRequestParams.containsKey("memberOfRoleWithCode")) {

                if (selectedOrganizationId != null) {

                    List<Role> roles = roleService.readByCode(allRequestParams.get("memberOfRoleWithCode"));

                    Criteria criteria = Criteria.where(UserConstants.USER_ROLE_ORG_MAPLIST).elemMatch(Criteria.where("orgId").is(selectedOrganizationId).and("roles").in(roles.get(0).getId()));

                    query.addCriteria(criteria);

                }
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(UserConstants.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;

            }
            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }

//        query.addCriteria(Criteria.where(UserConstants.STATE_FIELD_NAME).is(UserConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            Page<User> users = this.usersService.read(query, pageableRequest);

            return builder(success(users));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список пользователей", tags = {"Users"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,idn,name,middlename,surname,email,sex", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/search')")
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchUsers(@ApiParam(hidden = true)
                                         @RequestParam Map<String, String> allRequestParams,
                                         @RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken)
            throws InternalException {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = UserConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = UserConstants.DEFAUT_PAGE_SIZE;

            String sortBy = UserConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(UserConstants.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;

            }
            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            Page<User> users = this.usersService.search(allRequestParams.get("searchString"), pageableRequest);

            return builder(success(users));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить пользователь", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователь была найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная пользователь не найдена.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/read/{userId}')")
    @RequestMapping(value = "/read/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUser(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @PathVariable("userId") String userId) throws InternalException {
        try {
            User user = null;

            user = this.usersService.findUserById(userId);

            if (user == null) {
                return builder(errorWithDescription(null, "UserNotFound"));
            }

            return builder(success(user));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить цвета пользователя", tags = {"Users"})
    @RequestMapping(value = "/color/read", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUserColors() throws InternalException {
        try {

            return builder(success(UserConstants.USER_COLORS.values()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать пользователь", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Указывает, что пользователь была создана."),
            @ApiResponse(code = 400, message = "Указывает, что ИИН пользователя отсутствует.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createUser(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestBody @Valid User userRequest,
                                        BindingResult bindingResult, UriComponentsBuilder ucBuilder)
            throws InternalException {
        try {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            HttpHeaders headers = new HttpHeaders();
            if (bindingResult.hasErrors() || (userRequest == null)) {
                errors.addAllErrors(bindingResult);
                headers.add("errors", errors.toJSON());
                LOGGER.error("BindingResult hasError = true ");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BindingResult hasError = true [createUser]"));
            }

            if (userRequest.getIdn() == null) {
                LOGGER.error("ИИН не может быть нулевым.[userRequest.getIdn() == null] ");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "ИИН не может быть нулевым."));
            }

            // Проверьте, существует ли уже существующая пользователь с указанным ИИН, поэтому мы возвращаем CONFLICT
            if (usersService.readByIdn(userRequest.getIdn()).size() > 0) {
                LOGGER.error("Пользователь с ИИН '" + userRequest.getIdn() + "' уже существует.");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Пользователь с ИИН '" + userRequest.getIdn() + "' уже существует."));
            }

            User newUser = new User();

            newUser.setUsername(userRequest.getUsername());
            newUser.setEmail(userRequest.getEmail());
            newUser.setIdn(userRequest.getIdn());
            newUser.setName(userRequest.getName());
            newUser.setSurname(userRequest.getSurname());
            newUser.setMiddlename(userRequest.getMiddlename());
            newUser.setBirthday(userRequest.getBirthday());
            newUser.setAddress(userRequest.getAddress());
            newUser.setSex(userRequest.getSex());
            newUser.setMobilePhone(userRequest.getMobilePhone());
            newUser.setShowMobilePhone(userRequest.isShowMobilePhone());
            newUser.setContactPhone(userRequest.getContactPhone());
            newUser.setAboutYourself(userRequest.getAboutYourself());
            newUser.setInterfaceLang(userRequest.getInterfaceLang());
            newUser.setCategory(userRequest.getCategory());
            newUser.setExperience(userRequest.getExperience());
            //TODO надо писать проверку
            newUser.setSpecialityIds(userRequest.getSpecialityIds());
            newUser.setPens(userRequest.getPens());
            newUser.setInvalidity(userRequest.getInvalidity());
            newUser.setInsurPolicy(userRequest.getInsurPolicy());
            /**
             * i y.nurgali comment, because zhanserik clean up this properties in User model
             //TODO надо писать проверку
             newUser.setOrganizationIds(userRequest.getOrganizationIds());
             //TODO надо писать проверку
             newUser.setDoctorIds(userRequest.getDoctorIds());
             //TODO надо писать проверку
             newUser.setRightIds(userRequest.getRightIds());
             //TODO надо писать проверку
             newUser.setRoleIds(userRequest.getRoleIds());
             */

            //аудирование
            newUser.setCreatedBy("");
            newUser.setCreatedDate(LocalDateTime.now());

            newUser.setSysRegDate(LocalDateTime.now());

            //присвоение статуса
            newUser.setState(UserConstants.STATUS_ACTIVE);

            //Активность пользователя
            newUser.setActive(UserConstants.DEFAULT_IS_ACTIVE);

            this.usersService.create(newUser);
            headers.setLocation(ucBuilder.path("/users/read/{id}").buildAndExpand(newUser.getId()).toUri());
            return builder(success(newUser));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить пользователь", tags = {"Users"},
            notes = "Все значения запроса являются необязательными."
                    + "Например, вы можете включать атрибут name в тело JSON-объекта запроса, только обновляя name пользователя, оставляя все остальные поля незатронутыми. "
                    + "Когда атрибут явно включен и имеет значение null, значение пользователя будет обновлено до нуля. "
                    + "Пример: {\"name\": null} очистит name пользователя).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователь была обновлена."),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная пользователь не найдена."),
            @ApiResponse(code = 409, message = "Указывает, что запрашиваемая пользователь была обновлена одновременно или ИИН пользователь повторяется.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/update')")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUser(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestBody @Valid User userRequest, BindingResult bindingResult) throws InternalException {
        try {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            HttpHeaders headers = new HttpHeaders();
            if (bindingResult.hasErrors() || (userRequest == null)) {
                errors.addAllErrors(bindingResult);
                headers.add("errors", errors.toJSON());
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD_REQUEST"));
            }

            User currentUser = usersService.findUserById(userRequest.getId());
            if (currentUser == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.USER_NOT_FOUND, "Пользователь не существует."));
            }

            //TODO: бизнес правилы

            currentUser = userRequest;

//        // Проверьте, существует ли уже существующая пользователь с указанным ИИН, поэтому мы возвращаем CONFLICT
//        if (usersService.readByIdn(userRequest.getIdn()).size() > 0) {
//            throw new CloudocConflictException("Пользователь с ИИН '" + userRequest.getIdn()+ "' уже существует.");
//        }
//


//        if(userRequest.isUsernameChanged()) {
//            currentUser.setUsername(userRequest.getUsername());
//        }
//        if(userRequest.isEmailChanged()) {
//            currentUser.setEmail(userRequest.getEmail());
//        }
//        if(userRequest.isIdnChanged()) {
//            currentUser.setIdn(userRequest.getIdn());
//        }
//        if(userRequest.isNameChanged()) {
//            currentUser.setName(userRequest.getName());
//        }
//        if(userRequest.isSurnameChanged()) {
//            currentUser.setSurname(userRequest.getSurname());
//        }
//        if(userRequest.isMiddlenameChanged()) {
//            currentUser.setMiddlename(userRequest.getMiddlename());
//        }
//        if(userRequest.isBirthdayChanged()) {
//            currentUser.setBirthday(userRequest.getBirthday());
//        }
//        if(userRequest.isAddressChanged()) {
//            currentUser.setAddress(userRequest.getAddress());
//        }
//        if(userRequest.isSexChanged()) {
//            currentUser.setSex(userRequest.getSex());
//        }
//        if(userRequest.isMobilePhoneChanged()) {
//            currentUser.setMobilePhone(userRequest.getMobilePhone());
//        }
//        if(userRequest.isShowMobilePhoneChanged()) {
//            currentUser.setShowMobilePhone(userRequest.isShowMobilePhone());
//        }
//        if(userRequest.isContactPhoneChanged()) {
//            currentUser.setContactPhone(userRequest.getContactPhone());
//        }
//        if(userRequest.isAbout_yourselfChanged()) {
//            currentUser.setAbout_yourself(userRequest.getAbout_yourself());
//        }
//        if(userRequest.isInterface_langChanged()) {
//            currentUser.setInterface_lang(userRequest.getInterface_lang());
//        }
//        if(userRequest.isCategoryChanged()) {
//            currentUser.setCategory(userRequest.getCategory());
//        }
//        if(userRequest.isExperianceChanged()) {
//            currentUser.setExperiance(userRequest.getExperiance());
//        }
//        if(userRequest.isSpecialityIdsChanged()) {
//            //TODO надо писать проверку
//            currentUser.setSpecialityIds(userRequest.getSpecialityIds());
//        }
//        if(userRequest.isPensChanged()) {
//            currentUser.setPens(userRequest.getPens());
//        }
//        if(userRequest.isInvalidityChanged()) {
//            currentUser.setInvalidity(userRequest.getInvalidity());
//        }
//        if(userRequest.isInsurPolicyChanged()) {
//            currentUser.setInsurPolicy(userRequest.getInsurPolicy());
//        }
//        /**
//         * i y.nurgali comment, because zhanserik clean up this properties in User model
//        if(userRequest.isOrganizationIdsChanged()) {
//            //TODO надо писать проверку
//            currentUser.setOrganizationIds(userRequest.getOrganizationIds());
//        }
//        if(userRequest.isDoctorIdsChanged()) {
//            //TODO надо писать проверку
//            currentUser.setDoctorIds(userRequest.getDoctorIds());
//        }
//        if(userRequest.isRightIdsChanged()) {
//            //TODO надо писать проверку
//            currentUser.setRightIds(userRequest.getRightIds());
//        }
//        if(userRequest.isRoleIdsChanged()) {
//            //TODO надо писать проверку
//            currentUser.setRoleIds(userRequest.getRoleIds());
//        }*/

            this.usersService.update(currentUser);

            return builder(success(currentUser));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить пользователь updateShort", tags = {"Users"})
    @PutMapping("/updateShort")
    ResponseEntity<?> update(@RequestBody User user, BindingResult bindingResult) throws InternalException {
        try {
            usersService.updateUserShortInformation(user);
            return builder(success("updated"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Удалить пользователь", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Указывает, что пользователь была найдена и удалена. Тело ответа намеренно пуста."),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная пользователь не найдена.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/delete/{userId}')")
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteUser(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @ApiParam(name = "userId", value = "Идентификатор пользователя для удаления.")
    @PathVariable String userId) throws InternalException {
        try {

            User user = usersService.findUserById(userId);

            if (user == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }

            //Активность пользователя
            user.setActive(UserConstants.DEFAULT_IS_ACTIVE);

            user.setState(UserConstants.STATUS_DELETED);
            this.usersService.update(user);

            return builder(success("Success"));

        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Активировать пользователь", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Указывает, что пользователь была найдена и удалена. Тело ответа намеренно пуста."),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная пользователь не найдена.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/deactivate/{userId}')")
    @RequestMapping(value = "/deactivate/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deactivateUser(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @ApiParam(name = "userId", value = "Идентификатор пользователя для деактивирование.")
    @PathVariable String userId) throws InternalException {
        try {

            User user = usersService.findUserById(userId);

            if (user == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }

            //Активность пользователя
            user.setActive(UserConstants.DEFAULT_IS_ACTIVE);

            this.usersService.update(user);

            return builder(success("Success"));

        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Активировать пользователь", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Указывает, что пользователь была найдена и удалена. Тело ответа намеренно пуста."),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная пользователь не найдена.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/activate/{userId}')")
    @RequestMapping(value = "/activate/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> activateUser(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @ApiParam(name = "userId", value = "Идентификатор пользователя для активирование.")
    @PathVariable String userId) throws InternalException {
        try {

            User user = usersService.findUserById(userId);

            if (user == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }

            //Активность пользователя
            user.setActive(!UserConstants.DEFAULT_IS_ACTIVE);

            this.usersService.update(user);

            return builder(success("Success"));

        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить изображение пользователя", tags = {"Users"},
            notes = "Тело ответа содержит необработанные данные изображения, представляющие изображение пользователя. Тип содержимого типа ответа соответствует типу mimeType, который был задан при создании изображения.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователь был найден и имеет изображение, которое возвращается в теле."),
            @ApiResponse(code = 404, message = "Указывает, что запрашиваемый пользователь не найден или у пользователя нет изображения профиля. Описание состояния содержит дополнительную информацию об ошибке.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/picture/{userId}')")
    @RequestMapping(value = "/picture/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPicture(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken,
                                            @ApiParam(name = "userId", value = "Идентификатор пользователя для получения изображения.")
                                            @PathVariable String userId,
                                            HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {

            User user = usersService.findUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            GridFsResource resource = usersService.downloadFile(user.getAvatarId());

            if (resource == null) {
                throw new CloudocObjectNotFoundException("Пользователь с идентификатором '"
                        + user.getId() + "' не имеет изображения.", resource.getClass());
            }

            String contentType = resource.getContentType();


            HttpHeaders responseHeaders = new HttpHeaders();
            if (resource.getContentType() != null) {
                responseHeaders.set("Content-Type", resource.getContentType());
                contentType = resource.getContentType();
            } else {
                contentType = "image/jpeg";
                responseHeaders.set("Content-Type", "image/jpeg");
            }
//            return builder(success(ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource)));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("-CONTINUE-", "Ошибка экспорта изображения: " + e.getMessage());
            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, e.getMessage()));
        }

    }

    @ApiOperation(value = "Обновление изображения пользователя", tags = {"Users"}, consumes = "multipart/form-data",
            notes = "uploadUserPicture() method()")
    //@PostAuthorize("hasPermission(#accessToken, '/users/upload/{userId}')")
    @RequestMapping(value = "/upload/{userId}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserPicture(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestParam("file") MultipartFile file,
                                               @ApiParam(name = "userId", value = "Идентификатор пользователя для получения изображения.")
                                               @PathVariable String userId,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws IOException, InternalException {

        try {
            User user = usersService.findUserById(userId);
            if (user == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.USER_NOT_FOUND, "User not found"));
            }

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Требуется Multipart запрос."));
            }


            try {
                String mimeType = file.getContentType();
                String fileName = file.getName() + " - " + user.getId();
                String pictureId = usersService.saveUserPicture(file.getInputStream(), mimeType, fileName);
                LOGGER.debug("[uploadUserPicture()] - pictureId: " + pictureId);
                user.setAvatarId(pictureId);
                usersService.update(user);
            } catch (Exception e) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при чтении загруженного файла: " + e.getMessage()));
            }

            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновление изображения пользователя", tags = {"Users"}, consumes = "multipart/form-data",
            notes = "Запрос должен иметь тип multipart / form-data. В двоичном значении изображения должна быть одна часть файла. Кроме того, могут присутствовать следующие дополнительные поля формы:\n"
                    + "\n"
                    + "mimeType: Дополнительный mime-type для загруженного изображения. Если опустить, значение по умолчанию для image/jpeg используется в качестве mime-type для изображения.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователь был найден, и изображение обновлено. Тело ответа остается пустым намеренно."),
            @ApiResponse(code = 404, message = "Указывает, что запрашиваемый пользователь не найден.")
    })
    //@PostAuthorize("hasPermission(#accessToken, '/users/picture/{userId}')")
    @RequestMapping(value = "/picture/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserPicture(
            @RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestParam("file") MultipartFile file,
            @ApiParam(name = "userId", value = "Идентификатор пользователя для получения изображения.")
            @PathVariable String userId,
            HttpServletRequest request,
            HttpServletResponse response) throws InternalException {
        try {
            User user = usersService.findUserById(userId);
            if (user == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.USER_NOT_FOUND, "User not found"));
            }

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Требуется Multipart запрос."));
            }

//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//
//        if (multipartRequest.getFileMap().size() == 0) {
//            throw new CloudocIllegalArgumentException("Требуется Multipart запрос с содержимым файла");
//        }
//
//        MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

            try {

                String mimeType = file.getContentType();
                String pictureId = usersService.save(file.getInputStream(), mimeType, "temp", "username", "photo", mimeType);

//                String pictureId = usersService.saveUserPicture(file.getInputStream(), mimeType);
                LOGGER.debug("[updateUserPicture()] - pictureId: " + pictureId);
                user.setAvatarId(pictureId);

//            return new ResponseEntity<Void>(this.usersService.update(user));

            } catch (Exception e) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при чтении загруженного файла: " + e.getMessage()));
            }

            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Организации пользователя", tags = {"Users"})
    //@PostAuthorize("hasPermission(#accessToken, '/users/addorgwhithid')")
    @RequestMapping(value = "/addorgwhithid", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addOrgWhithId(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestBody @Valid UserRoleOrgMap requestUnit) throws InternalException {
        try {
            if (requestUnit != null
                    && requestUnit.getUserId() != null
                    && requestUnit.getOrgId() != null) {
                if (usersService.findUserById(requestUnit.getUserId()) != null)
                    return builder(success(usersService.addOrgWhithId(requestUnit)));
                else
                    LOGGER.error("BAD REQUEST - addOrgWhithId");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD REQUEST"));
            } else {
                LOGGER.error("BAD REQUEST - addOrgWhithId - 2");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD REQUEST"));
            }
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Роль пользователя в организации", tags = {"Users"})
    //@PostAuthorize("hasPermission(#accessToken, '/users/addrolewhithid')")
    @RequestMapping(value = "/addrolewhithid", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addRoleWhithId(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestBody @Valid UserOrgRoleUnit requestUnit) throws InternalException { // , @PathVariable String userId
        try {
            if (requestUnit != null
                    && requestUnit.getUserId() != null
                    && requestUnit.getOrgId() != null
                    && requestUnit.getRoleId() != null) {

                if (usersService.findUserById(requestUnit.getUserId()) == null)
                    return builder(errorWithDescription(ErrorCode.ErrorCodes.USER_NOT_FOUND, "User not found"));

                List<UserRoleOrgMap> curUnit = usersService.addRoleinOrg(requestUnit);

                return builder(success("Success"));

            } else {
                LOGGER.error("BAD REQUEST - addRoleWhithId");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD REQUEST"));
            }
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить организацию пользователя", tags = {"Users"})
    //@PostAuthorize("hasPermission(#accessToken, '/users/delorgwhithid')")
    @RequestMapping(value = "/delorgwhithid", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> delOrgWhithId(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestBody @Valid UserOrgRoleUnit requestUnit) throws InternalException {
        try {
            if (requestUnit != null
                    && requestUnit.getUserId() != null
                    && requestUnit.getOrgId() != null) {
                if (usersService.findUserById(requestUnit.getUserId()) != null)
                    return builder(success(usersService.delOrgWhithId(requestUnit)));
                else
                    LOGGER.error("BAD REQUEST - delOrgWhithId");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD REQUEST"));

            } else {
                LOGGER.error("BAD REQUEST - delOrgWhithId");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD REQUEST"));
            }
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Роль пользователя в организации", tags = {"Users"})
    //@PostAuthorize("hasPermission(#accessToken, '/users/delrolewhithid')")
    @RequestMapping(value = "/delrolewhithid", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> delRoleWhithId(@RequestHeader(value = AUTH_HEADER_NAME, required = false) final String accessToken, @RequestBody @Valid UserOrgRoleUnit requestUnit) throws InternalException { // , @PathVariable String userId
        try {
            if (requestUnit != null
                    && requestUnit.getUserId() != null
                    && requestUnit.getOrgId() != null
                    && requestUnit.getRoleId() != null) {

                if (usersService.findUserById(requestUnit.getUserId()) == null)
                    return builder(errorWithDescription(ErrorCode.ErrorCodes.USER_NOT_FOUND, "User not found"));

                List<UserRoleOrgMap> curUnit = usersService.delRoleinOrg(requestUnit);

                return builder(success("Success"));

            } else {
                LOGGER.error("BAD REQUEST - delOrgWhithId");
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "BAD REQUEST"));
            }
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}