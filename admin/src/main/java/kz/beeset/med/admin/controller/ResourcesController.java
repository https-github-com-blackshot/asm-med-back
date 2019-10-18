package kz.beeset.med.admin.controller;

import io.swagger.annotations.*;
import kz.beeset.med.admin.constant.ResourceConstant;
import kz.beeset.med.admin.constant.SecurityConstants;
import kz.beeset.med.admin.model.Resource;
import kz.beeset.med.admin.service.IResourceService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/resource")
@Api(tags = {"Resource"}, description = "Ресурсы", authorizations = {@Authorization(value = "bearerAuth")})
public class ResourcesController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesController.class);

    @Autowired
    private IResourceService resourceService;

    @ApiOperation(value = "Получить список ресурсов", tags = {"Resource"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllResources(@RequestParam(value = "types", required = false) List<String> types) {
        try {
            return builder(success(resourceService.getAllResources(types)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Get resource type list", tags = {"Resource"})
    @GetMapping("/typelist")
    public ResponseEntity<?> getAllResourcesTypes() {
        try {
            return builder(success(resourceService.getResourceTypeList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ресурсов в виде списка", tags = {"Resource"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string",
                    value = "Только возвращает ресурса с данным id", paramType = "query"),
            @ApiImplicitParam(name = "type", dataType = "string",
                    value = "Только возвращает ресурса с данным type", allowableValues = "menu,controller,widget", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string",
                    value = "Только возвращает ресурса с данным code", paramType = "query"),
            @ApiImplicitParam(name = "descriptionLike", dataType = "string",
                    value = "Только возвращает ресурса с таким description, таким как заданное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "descriptionRuLike", dataType = "string",
                    value = "Только возвращает ресурса с таким descriptionRu, как данное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "descriptionKzLike", dataType = "string",
                    value = "Только возвращает ресурса с таким descriptionKz, как данное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "descriptionEnLike", dataType = "string",
                    value = "Только возвращает ресурса с таким descriptionEn, как данное значение. Используйте '%' в качестве символа подстановки.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string",
                    value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,type", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int",
                    value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int",
                    value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что ресурса существуют и возвращает.")
    })
    @RequestMapping(value = "/readInList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getResourcesInList(@ApiParam(hidden = true)
                                                @RequestParam Map<String, String> allRequestParams) {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = ResourceConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = ResourceConstant.DEFAUT_PAGE_SIZE;

            String sortBy = ResourceConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(ResourceConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("type")) {
                query.addCriteria(Criteria.where(ResourceConstant.TYPE_FIELD_NAME).is(allRequestParams.get("type")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(ResourceConstant.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("descriptionLike")) {
                query.addCriteria(Criteria.where(ResourceConstant.DESCRIPTION_FIELD_NAME).regex(allRequestParams.get("descriptionLike")));
            }
            if (allRequestParams.containsKey("descriptionRuLike")) {
                query.addCriteria(Criteria.where(ResourceConstant.DESCRIPTIONRU_FIELD_NAME).regex(allRequestParams.get("descriptionRuLike")));
            }
            if (allRequestParams.containsKey("descriptionKzLike")) {
                query.addCriteria(Criteria.where(ResourceConstant.DESCRIPTIONKZ_FIELD_NAME).regex(allRequestParams.get("descriptionKzLike")));
            }
            if (allRequestParams.containsKey("descriptionEnLike")) {
                query.addCriteria(Criteria.where(ResourceConstant.DESCRIPTIONEN_FIELD_NAME).regex(allRequestParams.get("descriptionEnLike")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(ResourceConstant.SORT_DIRECTION_DESC))
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

            Page<Resource> resources = this.resourceService.read(query, pageableRequest);

            return builder(success(resources));

        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ресурсов в виде дерево", tags = {"Resource"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", dataType = "string",
                    value = "Только возвращает ресурса с данным type", allowableValues = "menu,controller,widget", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что ресурса существуют и возвращает.")
    })
    @RequestMapping(value = "/readInTree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getResourcesInTree(@ApiParam(hidden = true)
                                                @RequestParam Map<String, String> allRequestParams,
                                                Locale locale,
                                                HttpServletRequest request) {
        try {
            LOGGER.info("ResourcesController.getResourcesInTree");
            final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
            Query query = new Query();

            if (allRequestParams.containsKey("type")) {
                query.addCriteria(Criteria.where(ResourceConstant.TYPE_FIELD_NAME).is(allRequestParams.get("type")));
            }

            List<?> resources = this.resourceService.readByTree(query, allRequestParams.get("type"), locale, token);

            return builder(success(resources));

        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Get resource module type list", tags = {"Resource"})
    @GetMapping("/moduleTypes")
    public ResponseEntity<?> getModuleType() {
        try {
            return builder(success(resourceService.getModuleType()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ресурсов tree based", tags = {"Resource"})
    @GetMapping("/tree")
    public ResponseEntity<?> getAllResourcesTree() {
        try {
            return builder(success(resourceService.getTreeBased()));
        } catch (InternalException e) {
            LOGGER.error("getAllResourcesTree: " + e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ресурсов tree folder based", tags = {"Resource"})
    @GetMapping("/treefolder")
    public ResponseEntity<?> getAllResourcesTreeFolder() {
        try {
            return builder(success(resourceService.getTreeFolderBased()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить ресурс по ID", tags = {"Resource"})
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getResourceById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(resourceService.getResourceById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить ресурсов по parentId", tags = {"Resource"})
    @GetMapping("/byparent/{id}")
    public ResponseEntity<?> getAllResourcesByParentId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(resourceService.getAllByParentId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage() + " :getAllResourcesByParentId: ", e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать либо обновить ресурс", tags = {"Resource"})
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setResource(@Valid @RequestBody Resource resource) {
        try {
            return builder(success(resourceService.setResourse(resource)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать ресурс by list", tags = {"Resource"})
    @RequestMapping(value = "/addlist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setResourceList(@Valid @RequestBody List<Resource> list) {
        try {
            return builder(success(resourceService.setResourceList(list)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удаление ресурса по id")
    ResponseEntity<?> delete(@ApiParam("ID ресурса") @PathVariable(name = "id") String resourceId) throws InternalException {
        try {
            resourceService.deleteResourceById(resourceId);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}
