package kz.beeset.med.admin.controller;

import kz.beeset.med.admin.model.catalog.Catalog;
import kz.beeset.med.admin.model.catalog.City;
import kz.beeset.med.admin.model.catalog.PAEventType;
import kz.beeset.med.admin.model.catalog.PAStatus;
import kz.beeset.med.admin.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiOperation;
import kz.beeset.med.admin.service.ICatalogService;
import kz.beeset.med.admin.utils.CommonService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
public class CatalogController  extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class);

    @Autowired
    ICatalogService catalogService;

    @ApiOperation(value = "Получить список catalog", tags = {"Catalog"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
        @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", paramType = "query"),
        @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
        @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @GetMapping("/list")
    public ResponseEntity<?> getCatalogList(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(catalogService.getCatalogList(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить catalog по ID", tags = {"Catalog"})
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getCatalogById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(catalogService.getCatalogById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить catalog по url code", tags = {"Catalog"})
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getCatalogByCode(@PathVariable(name = "code") String code) {
        try {
            return builder(success(catalogService.getCatalogByCode(code)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать catalog", tags = {"Catalog"})
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setCatalog(@Valid @RequestBody Catalog catalog) {
        try {
            return builder(success(catalogService.setCatalog(catalog)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setResourse: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Удаление catalog по id", tags = {"Catalog"})
    ResponseEntity<?> deleteCatalog(@ApiParam("ID catalog") @PathVariable(name = "id") String id) throws InternalException {
        try {
            catalogService.deleteCatalogById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список patient appeal event type", tags = {"Catalog"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @GetMapping("/eventType/list")
    public ResponseEntity<?> getEventTypeList(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(catalogService.getEventTypeList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить event type по ID", tags = {"Catalog"})
    @GetMapping("/eventType/by/{id}")
    public ResponseEntity<?> getEventTypeById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(catalogService.getEventTypeById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать eventType", tags = {"Catalog"})
    @RequestMapping(value = "/eventType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setEventType(@Valid @RequestBody PAEventType eventType) {
        try {
            return builder(success(catalogService.setEventType(eventType)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setEventType: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/eventType/delete/{id}")
    @ApiOperation(value = "Удаление eventType по id", tags = {"Catalog"})
    ResponseEntity<?> deleteEventType(@ApiParam("ID eventType") @PathVariable(name = "id") String id) throws InternalException {
        try {
            catalogService.deleteEventTypeById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список patient appeal status", tags = {"Catalog"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @GetMapping("/status/list")
    public ResponseEntity<?> getStatusList(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(catalogService.getStatusList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить status по ID", tags = {"Catalog"})
    @GetMapping("/status/by/{id}")
    public ResponseEntity<?> getStatusById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(catalogService.getStatusById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать status", tags = {"Catalog"})
    @RequestMapping(value = "/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setStatus(@Valid @RequestBody PAStatus status) {
        try {
            return builder(success(catalogService.setStatus(status)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setStatus: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/status/delete/{id}")
    @ApiOperation(value = "Удаление status по id", tags = {"Catalog"})
    ResponseEntity<?> deleteStatus(@ApiParam("ID status") @PathVariable(name = "id") String id) throws InternalException {
        try {
            catalogService.deleteStatusById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список city", tags = {"Catalog"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @GetMapping("/city/list")
    public ResponseEntity<?> getCityList(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(catalogService.getCityList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить status по ID", tags = {"Catalog"})
    @GetMapping("/city/by/{id}")
    public ResponseEntity<?> getCityById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(catalogService.getCityById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать city", tags = {"Catalog"})
    @RequestMapping(value = "/city", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setCity(@Valid @RequestBody City city) {
        try {
            return builder(success(catalogService.setCity(city)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setStatus: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/city/delete/{id}")
    @ApiOperation(value = "Удаление city по id", tags = {"Catalog"})
    ResponseEntity<?> deleteCity(@ApiParam("ID city") @PathVariable(name = "id") String id) throws InternalException {
        try {
            catalogService.deleteCityById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
