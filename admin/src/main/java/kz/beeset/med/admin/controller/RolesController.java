package kz.beeset.med.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import kz.beeset.med.admin.model.Role;
import kz.beeset.med.admin.service.IRoleService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = {"Role"}, description = "Управление new ролями", authorizations = {@Authorization(value = "bearerAuth")})
public class RolesController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RightsController.class);
    @Autowired
    IRoleService roleService;

    @ApiOperation(value = "Получить список ролей", tags = {"Role"})
    @GetMapping("/read")
    public ResponseEntity<?> getAllResources() {
        try {
            return builder(success(roleService.read()));
        } catch (InternalException e) {
            LOGGER.error("getAllResources:" + e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ролей с помощью кода", tags = {"Role"})
    @GetMapping("/byCode/{code}")
    public ResponseEntity<?> readByCode(@PathVariable(name = "code") String code) {
        try {
            return builder(success(roleService.readByCode(code)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ролей с помощью id", tags = {"Role"})
    @GetMapping("/byId/{id}")
    public ResponseEntity<?> findRoleById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(roleService.findRoleById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список прав с помощью роль id", tags = {"Role"})
    @GetMapping("/rights/{roleId}")
    public ResponseEntity<?> findRigthsByRoleId(@PathVariable(name = "roleId") String roleId) {
        try {
            return builder(success(roleService.getRigthListByRoleId(roleId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать роль", tags = {"Role"})
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setRole(@Valid @RequestBody Role role) {
        try {
            return builder(success(roleService.create(role)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setRole: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить роль", tags = {"Role"})
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateRole(@Valid @RequestBody Role role) {
        try {
            return builder(success(roleService.update(role)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving updateRole: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ролей tree based", tags = {"Role"})
    @GetMapping("/tree/rights/{id}")
    public ResponseEntity<?> getTreeFolderBasedRigth(@PathVariable(value = "id") String roleId) {
        try {
            return builder(success(roleService.getTreeFolderBasedRigth(roleId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Добавить права в роли", tags = {"Role"})
    @RequestMapping(value = "/add/rights/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setRoleRights(@Valid @RequestBody List<String> rights,
                                           @PathVariable(value = "id") String id) {
        try {
            return builder(success(roleService.setRoleRights(rights, id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удаление роли по id")
    ResponseEntity<?> delete(@ApiParam("ID роли") @PathVariable(name = "id") String roleId) throws InternalException {
        try {
            roleService.deleteRoleById(roleId);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/deleteRigth/{roleId}/{rightId}")
    @ApiOperation("Удаление прав по roleId")
    ResponseEntity<?> delete(@ApiParam("ID роли") @PathVariable(name = "roleId") String roleId,
                             @PathVariable(name = "rightId") String rightId) throws InternalException {
        try {
            roleService.deleteRoleRight(roleId, rightId);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}
