package kz.beeset.med.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import kz.beeset.med.admin.model.Right;
import kz.beeset.med.admin.service.IRightService;
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
@RequestMapping("/right")
@Api(tags = {"Right"}, description = "Rights", authorizations = {@Authorization(value = "bearerAuth")})
public class RightsController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RightsController.class);
    @Autowired
    IRightService rightsService;

    @ApiOperation(value = "Get right list", tags = {"Right"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllRights() {
        try {
            return builder(success(rightsService.getAllRights()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Get right list (tree based)", tags = {"Right"})
    @GetMapping("/tree")
    public ResponseEntity<?> getAllRightTree() {
        try {
            return builder(success(rightsService.getTreeBased()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Get right by id", tags = {"Right"})
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getRightById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(rightsService.getRightById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Get resources  rightid", tags = {"Right"})
    @GetMapping("/resources/{rightId}")
    public ResponseEntity<?> getResourcesByRightId(@PathVariable(name = "rightId") String rightId) {
        try {
            return builder(success(rightsService.getResourcesByRightId(rightId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Get rights by parent id", tags = {"Right"})
    @GetMapping("/byparent/{id}")
    public ResponseEntity<?> getAllRightByParentId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(rightsService.getAllByParentId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Create&update right", tags = {"Right"})
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setRight(@Valid @RequestBody Right right) {
        try {
            return builder(success(rightsService.setRight(right)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Import right", tags = {"Right"})
    @RequestMapping(value = "/add/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setRightList(@Valid @RequestBody List<Right> list) {
        try {
            return builder(success(rightsService.setRightList(list)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Get rigth resources (tree based)", tags = {"Right"})
    @GetMapping("/tree/resources/{id}")
    public ResponseEntity<?> getTreeRightResources(@PathVariable(value = "id") String rightId) {
        try {
            return builder(success(rightsService.getTreeRightResources(rightId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Add right resources", tags = {"Right"})
    @RequestMapping(value = "/add/resources/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setRightResources(@Valid @RequestBody List<String> resources,
                                               @PathVariable(value = "id") String id) {
        try {
            return builder(success(rightsService.setRightResources(resources, id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удаление право по id")
    ResponseEntity<?> delete(@ApiParam("ID роли") @PathVariable(name = "id") String rightId) throws InternalException {
        try {
            rightsService.deleteRightById(rightId);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{rightId}/resource/{resourceId}")
    @ApiOperation("Удаление право по id")
    ResponseEntity<?> deleteResource(@ApiParam("ID роли") @PathVariable(name = "rightId") String rightId,@PathVariable(name = "resourceId") String resId) throws InternalException {
        try {
            rightsService.deleteResourceInRight(rightId,resId);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
