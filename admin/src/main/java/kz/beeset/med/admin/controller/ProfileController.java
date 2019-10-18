package kz.beeset.med.admin.controller;

import io.swagger.annotations.*;
import kz.beeset.med.admin.model.Profile;
import kz.beeset.med.admin.service.IProfileService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/profiles")
@Api(tags = {"Profiles"}, description = "Управление profiles", authorizations = {@Authorization(value = "bearerAuth")})
public class ProfileController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private IProfileService profileService;

    @ApiOperation(value = "Получить объект/список Profile по параметрам", tags = {"Profiles"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает объект с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "healthStatus", dataType = "int", value = "Возвращает объект с данным healthStatus", paramType = "query"),
            @ApiImplicitParam(name = "doctor", dataType = "string", value = "Возвращает объект с данным doctorUserId", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "string", value = "Возвращает объект с данным state", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMP существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/profile/pageable/byParams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProfilePageableByParams(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(profileService.readProfilesPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список Profile по параметрам", tags = {"Profiles"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMP существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/profile/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProfilePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(profileService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех Profiles", tags = {"Profiles"})
    @RequestMapping(value = "read/profile/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProfileIterable() {
        try {
            return builder(success(profileService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить Profile по заданному ID", tags = {"Profiles"})
    @RequestMapping(value = "read/profile/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProfileById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(profileService.getById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить Profile по заданному User ID", tags = {"Profiles"})
    @RequestMapping(value = "read/profile/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProfileByUserId(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(profileService.getByUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект Profile", tags = {"Profiles"})
    @RequestMapping(value = "create/profile", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createProfile(@RequestBody @Valid Profile profile) {
        try {
            return builder(success(profileService.createProfile(profile)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Remove объект Profile", tags = {"Profiles"})
    @RequestMapping(value = "delete/profile/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteProfile(@PathVariable(name = "id") String id) {
        try {
            profileService.removeProfileById(id);
            return builder(success("Removed successfully"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


}