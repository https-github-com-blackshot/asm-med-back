package kz.beeset.med.admin.controller;


import io.swagger.annotations.*;
import kz.beeset.med.admin.constant.OrganizationConstant;
import kz.beeset.med.admin.constant.UserConstants;
import kz.beeset.med.admin.model.Organization;
import kz.beeset.med.admin.service.IOrganizationService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.BindingErrorsResponse;
import kz.beeset.med.admin.utils.error.CloudocConflictException;
import kz.beeset.med.admin.utils.error.CloudocIllegalArgumentException;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.model.TreeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org")
@Api(tags = {"Org"}, description = "Управление организациями", authorizations = {@Authorization(value = "bearerAuth")})
public class OrganizationsController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationsController.class);

    @Autowired
    IOrganizationService organizationService;

    String sortBy = OrganizationConstant.ID_FIELD_NAME;

    @ApiOperation("Возв. списка с выборкой ?param1=param1&param2=param2...")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает организаций с заданным id",
                    paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает организаций с заданным code",
                    paramType = "query"),
            @ApiImplicitParam(name = "nameKz", dataType = "string", value = "Возвращает организаций с заданным nameKz",
                    paramType = "query"),
            @ApiImplicitParam(name = "nameRu", dataType = "string", value = "Возвращает организаций с заданным nameRu",
                    paramType = "query"),
            @ApiImplicitParam(name = "nameEn", dataType = "string", value = "Возвращает организаций с заданным nameEn",
                    paramType = "query"),
            @ApiImplicitParam(name = "nameKzLike", dataType = "string", value = "Возвращает организаций содержаший " +
                    "заданный nameKz.", paramType = "query"),
            @ApiImplicitParam(name = "nameRuLike", dataType = "string", value = "Возвращает организаций содержаший " +
                    "заданный nameRu.", paramType = "query"),
            @ApiImplicitParam(name = "nameEnLike", dataType = "string", value = "Возвращает организаций содержаший " +
                    "заданный nameEn.", paramType = "query"),
            @ApiImplicitParam(name = "memberOfRightWithId", dataType = "string", value = "Возвращает организаций " +
                    "которые являются членами заданных прав Id.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет " +
                    "использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu," +
                    "nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки." +
                    "(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать." +
                    "(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.." +
                    "(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что организаций существуют и возвращает.")
    })
    @RequestMapping(value = "read", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<?> read(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams,
                           HttpServletRequest request) {
        try {


            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = OrganizationConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = OrganizationConstant.DEFAUT_PAGE_SIZE;

            String sortBy = OrganizationConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where("id").is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where("code").is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("nameKz")) {
                query.addCriteria(Criteria.where("nameKz").is(allRequestParams.get("nameKz")));
            }
            if (allRequestParams.containsKey("nameRu")) {
                query.addCriteria(Criteria.where("nameRu").is(allRequestParams.get("nameRu")));
            }
            if (allRequestParams.containsKey("nameEn")) {
                query.addCriteria(Criteria.where("nameEn").is(allRequestParams.get("nameEn")));
            }
            if (allRequestParams.containsKey("nameKzLike")) {
                query.addCriteria(Criteria.where("nameKz").regex(allRequestParams.get("nameKzLike")));
            }
            if (allRequestParams.containsKey("nameRuLike")) {
                query.addCriteria(Criteria.where("nameRu").regex(allRequestParams.get("nameRuLike")));
            }
            if (allRequestParams.containsKey("nameEnLike")) {
                query.addCriteria(Criteria.where("nameEn").regex(allRequestParams.get("nameEnLike")));
            }
            if (allRequestParams.containsKey("memberOfRightWithId")) {
                query.addCriteria(Criteria.where(allRequestParams.get("memberOfRightWithId")).in("rightIds"));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(OrganizationConstant.SORT_DIRECTION_DESC)) ;
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

            query.addCriteria(Criteria.where("state").is(OrganizationConstant.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return builder(success(organizationService.read(query, pageableRequest)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @GetMapping("/read/{id}")
    ResponseEntity<?> readById(@PathVariable(name = "id") String id) throws InternalException {
        try {
            return builder(success(organizationService.getSingleOrgUnit(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "read/tree", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Возв. списка в виде дерева")
    ResponseEntity<?> tree(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(this.organizationService.tree()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/rootOrganization", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<?> getRootOrganization() {
        try {

            String rootOrganizationId = UserConstants.ROOT_ORGANIZATION_ID;

            Organization organization = organizationService.getSingleOrgUnit(rootOrganizationId);

            return builder(success(organization));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "read/all", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Возв. списка")
    ResponseEntity<?> readAll() throws InternalException {
        try {
            return builder(success(organizationService.read()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "read/allButRoot", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Возв. списка кроме Корневой организации")
    ResponseEntity<?> readAllButRoot() throws InternalException {
        try {
            return builder(success(organizationService.readButRoot()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
//    @RequestMapping(value = "readpag", params = {"page", "size"}, method = GET)
//    @ResponseBody
//    public TreeTableReturn findPaginated(
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//
//        return orgUnitService.findPaginated(page, size);
//
//    }

    @PostMapping("/create")
    @ApiOperation("Создание организаций")
    ResponseEntity<?> create(@ApiParam("Обьект JSON организации") @RequestBody Organization organizationRequest,
                             BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
        try {
            //Organization orgUnit = fromDto(orgPersonDto);

            HttpHeaders headers = new HttpHeaders();

            BindingErrorsResponse errors = new BindingErrorsResponse();

            if (bindingResult.hasErrors() || (organizationRequest == null)) {

                errors.addAllErrors(bindingResult);

                headers.add("errors", errors.toJSON());

                return new ResponseEntity<Organization>(headers, HttpStatus.BAD_REQUEST);

            }

            if (organizationRequest.getCode() == null) {

                throw new CloudocIllegalArgumentException("Код не может быть нулевым.");

            }

            if (organizationService.readByCode(organizationRequest.getCode()).size() > 0) {
                throw new CloudocConflictException("Меню с кодом '" + organizationRequest.getCode() +
                        "' уже существует.");
            }

            Organization newMenu = organizationService.create(organizationRequest);

            headers.setLocation(ucBuilder.path("/org/read/{id}").buildAndExpand(newMenu.getId()).toUri());

            return builder(success(newMenu));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @PutMapping("/update")
    @ApiOperation("Обновить")
    ResponseEntity<?> update(@RequestBody Organization organizationRequest, BindingResult bindingResult)
            throws InternalException {
        try {
            HttpHeaders headers = new HttpHeaders();

            BindingErrorsResponse errors = new BindingErrorsResponse();

            if (bindingResult.hasErrors() || (organizationRequest == null)) {

                errors.addAllErrors(bindingResult);

                headers.add("errors", errors.toJSON());

                return new ResponseEntity<Organization>(headers, HttpStatus.BAD_REQUEST);

            }

            if (organizationService.get(organizationRequest.getId()) == null)
                return new ResponseEntity<Organization>(HttpStatus.NOT_FOUND);

            Organization updatedMenu = organizationService.updateNew(organizationRequest);

            return builder(success(updatedMenu));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

//    @DeleteMapping("/delete")
//    @ApiOperation("Удалить 'state = ?5'")
//    void delete(@RequestBody Organization orgUnit) {
//        orgUnitService.delete(orgUnit);
//    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удаление пользователя по id")
    ResponseEntity<?> delete(@ApiParam("ID организации") @PathVariable(name = "id") String orgId) {
        try {
            Organization organization = organizationService.get(orgId);
            organizationService.delete(organization);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список пользователей", tags = {"Org"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска",
                    paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое " +
                    "будет использоваться вместе с order.", allowableValues = "id,code,nameRu,nameKz,nameEn",
                    paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.",
                    paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.",
                    paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchUsers(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = OrganizationConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = OrganizationConstant.DEFAUT_PAGE_SIZE;

            String sortBy = OrganizationConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(OrganizationConstant.SORT_DIRECTION_DESC))
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

            List<TreeData> users = this.organizationService.search(allRequestParams.get("searchString"),
                    pageableRequest);

            return builder(success(users));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}
