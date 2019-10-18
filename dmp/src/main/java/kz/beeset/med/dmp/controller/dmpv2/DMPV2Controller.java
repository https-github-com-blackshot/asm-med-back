package kz.beeset.med.dmp.controller.dmpv2;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.dmpv2.*;
import kz.beeset.med.dmp.service.IDMPV2Service;
import kz.beeset.med.dmp.service.dmpv2.*;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.CloudocObjectNotFoundException;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dmpv2/main")
@Api(tags = {"DMPV2"}, description = "DMPV2", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPV2Controller extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2Controller.class);

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IDiseaseService diseaseService;
    @Autowired
    private ILaboratoryService laboratoryService;
    @Autowired
    private IMedicineService medicineService;
    @Autowired
    private IDiagnosticsService diagnosticsService;
    @Autowired
    private IDMPV2Service dmpV2Service;
    @Autowired
    private IProceduresAndInterventionsService proceduresAndInterventionsService;
    @Autowired
    private IProtocolService protocolService;

    /******************************************************************************************************
     * CATEGORY Services
     */

    @ApiOperation(value = "Получить объект/список категорий по параметрам с пагинацией", tags = {"CATEGORY"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "filter", dataType = "string", value = "Возвращает список объектов с данным фильтром", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов CATEGORY существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/category/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readCategoryPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(categoryService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список категорий", tags = {"CATEGORY"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/category/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchCategoryPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(categoryService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех категорий", tags = {"CATEGORY"})
    @RequestMapping(value = "read/category/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategoryIterable() {
        try {
            return builder(success(categoryService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех фильтров по категориям", tags = {"CATEGORY"})
    @RequestMapping(value = "read/category/filter/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategoriesFilterIterable() {
        try {
            return builder(success(CategoriesFilter.getFilter()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех категорий по фильтру", tags = {"CATEGORY"})
    @RequestMapping(value = "read/category/iterable/byFilter/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategoryIterableByFilter(@PathVariable(name = "filter") String filter) {
        try {
            return builder(success(categoryService.readIterableByFilter(filter)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить категорию по заданному ID", tags = {"CATEGORY"})
    @RequestMapping(value = "read/category/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategory(@PathVariable(name = "id") String id) {
        try {
            return builder(success(categoryService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую категорию", tags = {"CATEGORY"})
    @RequestMapping(value = "create/category", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createCategory(@RequestBody @Valid Category category) {
        try {
            return builder(success(categoryService.create(category)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить существующую категорию", tags = {"CATEGORY"})
    @RequestMapping(value = "update/category", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid Category category) {
        try {
            return builder(success(categoryService.update(category)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить категорию", tags = {"CATEGORY"})
    @RequestMapping(value = "delete/category/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") String id) {
        try {
            categoryService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * Diagnostics Services
     */

    @ApiOperation(value = "Получить объект/список диагностики по параметрам с пагинацией", tags = {"DIAGNOSTICS"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", dataType = "string", value = "Возвращает список объектов с данным категорий", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов Diagnostics существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/diagnostics/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readDiagnosticsPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(diagnosticsService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список диагностики", tags = {"DIAGNOSTICS"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/diagnostics/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDiagnosticsPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(diagnosticsService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех диагностики", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "read/diagnostics/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiagnosticsIterable() {
        try {
            return builder(success(diagnosticsService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список диагностик по заданным ID", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "read/diagnostics/byIdIn/{ids}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiagnosticsIterableByIdIn(@PathVariable List<String> ids) {
        try {
            return builder(success(diagnosticsService.readIterableByIdIn(ids)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех диагностики по заданному списку", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "read/diagnostics/iterable/byIdIn/{selectedIds}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiagnosticsIterable(@PathVariable List<String> selectedIds) {
        try {
            return builder(success(diagnosticsService.readCategorizedDiagnosticsByIdIn(selectedIds)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех диагностических методов по категориям", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "read/categorized/diagnostics", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategorizedDiagnostics() {
        try {
            return builder(success(diagnosticsService.readCategorizedDiagnostics()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех диагностики по категорий", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "read/diagnostics/iterable/byCategoryId/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiagnosticsIterableByCategoryId(@PathVariable(name = "filter") String categoryId) {
        try {
            return builder(success(diagnosticsService.readIterableByCategoryId(categoryId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить диагностику по заданному ID", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "read/diagnostics/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiagnostics(@PathVariable(name = "id") String id) {
        try {
            return builder(success(diagnosticsService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую диагностику", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "create/diagnostics", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDiagnostics(@RequestBody @Valid Diagnostics diagnostics) {
        try {
            return builder(success(diagnosticsService.create(diagnostics)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить существующую диагностику", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "update/diagnostics", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDiagnostics(@RequestBody @Valid Diagnostics diagnostics) {
        try {
            return builder(success(diagnosticsService.update(diagnostics)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить диагностику", tags = {"DIAGNOSTICS"})
    @RequestMapping(value = "delete/diagnostics/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDiagnostics(@PathVariable(name = "id") String id) {
        try {
            diagnosticsService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    /******************************************************************************************************
     * Disease Services
     */

    @ApiOperation(value = "Получить объект/список болезнь по параметрам с пагинацией", tags = {"DISEASE"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", dataType = "string", value = "Возвращает список объектов с данным категорий", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов Diagnostics существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/disease/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readDiseasePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(diseaseService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список болезни", tags = {"DISEASE"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/disease/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDiseasePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(diseaseService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех болезни", tags = {"DISEASE"})
    @RequestMapping(value = "read/disease/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiseaseIterable() {
        try {
            return builder(success(diseaseService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех болезни по фильтру", tags = {"DISEASE"})
    @RequestMapping(value = "read/disease/iterable/byCategoryId/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiseaseIterableByCategoryId(@PathVariable(name = "filter") String categoryId) {
        try {
            return builder(success(diseaseService.readIterableByCategoryId(categoryId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список болезней по заданному списку ID", tags = {"DISEASE"})
    @RequestMapping(value = "read/disease/iterable/byIdIn/{ids}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDiseasesByIdIn(@PathVariable(name = "ids") List<String> ids) {
        try {
            return builder(success(diseaseService.readIterableByIdIn(ids)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить болезнь по заданному ID", tags = {"DISEASE"})
    @RequestMapping(value = "read/disease/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDisease(@PathVariable(name = "id") String id) {
        try {
            return builder(success(diseaseService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую болезнь", tags = {"DISEASE"})
    @RequestMapping(value = "create/disease", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDisease(@RequestBody @Valid Disease disease) {
        try {
            return builder(success(diseaseService.create(disease)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить существующую болезнь", tags = {"DISEASE"})
    @RequestMapping(value = "update/disease", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDisease(@RequestBody @Valid Disease disease) {
        try {
            return builder(success(diseaseService.update(disease)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить диагностику", tags = {"DISEASE"})
    @RequestMapping(value = "delete/disease/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDisease(@PathVariable(name = "id") String id) {
        try {
            diseaseService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * Laboratory Services
     */

    @ApiOperation(value = "Получить объект/список лабораторий  по параметрам с пагинацией", tags = {"LABORATORY"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", dataType = "string", value = "Возвращает список объектов с данным категорий", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов Laboratory существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/laboratory/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readLaboratoryPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(laboratoryService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список лабораторий", tags = {"LABORATORY"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/laboratory/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchLaboratoryPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(laboratoryService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лабораторий", tags = {"LABORATORY"})
    @RequestMapping(value = "read/laboratory/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLaboratoryIterable() {
        try {
            return builder(success(laboratoryService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список лабораторий по заданным ID", tags = {"LABORATORY"})
    @RequestMapping(value = "read/laboratory/byIdIn/{ids}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLaboratoryIterableByIdIn(@PathVariable List<String> ids) {
        try {
            return builder(success(laboratoryService.readIterableByIdIn(ids)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лабораторий по указанному списку", tags = {"LABORATORY"})
    @RequestMapping(value = "read/laboratory/iterable/byIdIn/{selectedIds}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategorizedLaboratoryIterableByIdIn(@PathVariable List<String> selectedIds) {
        try {
            return builder(success(laboratoryService.readCategorizedLaboratoriesByIdIn(selectedIds)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лабораторий по категориям", tags = {"LABORATORY"})
    @RequestMapping(value = "read/categorized/laboratories", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategorizedLaboratories() {
        try {
            return builder(success(laboratoryService.readCategorizedLaboratories()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лабораторий по фильтру", tags = {"LABORATORY"})
    @RequestMapping(value = "read/laboratory/iterable/byCategoryId/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLaboratoryIterableByCategoryId(@PathVariable(name = "filter") String categoryId) {
        try {
            return builder(success(laboratoryService.readIterableByCategoryid(categoryId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить лабораторий по заданному ID", tags = {"LABORATORY"})
    @RequestMapping(value = "read/laboratory/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLaboratory(@PathVariable(name = "id") String id) {
        try {
            return builder(success(laboratoryService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую лабораторий", tags = {"LABORATORY"})
    @RequestMapping(value = "create/laboratory", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createLaboratory(@RequestBody @Valid Laboratory laboratory) {
        try {
            return builder(success(laboratoryService.create(laboratory)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить существующую лабораторий", tags = {"LABORATORY"})
    @RequestMapping(value = "update/laboratory", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateLaboratory(@RequestBody @Valid Laboratory laboratory) {
        try {
            return builder(success(laboratoryService.update(laboratory)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить лабораторий", tags = {"LABORATORY"})
    @RequestMapping(value = "delete/laboratory/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteLaboratory(@PathVariable(name = "id") String id) {
        try {
            laboratoryService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * Procedures and Interventions Services
     */

    @ApiOperation(value = "Получить объект/список процедур и вмешательств  по параметрам с пагинацией", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", dataType = "string", value = "Возвращает список объектов с данным категорий", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов Laboratory существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/procedures-and-interventions/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readProceduresAndInterventionsPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(proceduresAndInterventionsService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список процедур и вмешательств", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/procedures-and-interventions/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchProceduresAndInterventionsPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(proceduresAndInterventionsService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех процедур и вмешательств", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "read/procedures-and-interventions/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProceduresAndInterventionsIterable() {
        try {
            return builder(success(proceduresAndInterventionsService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список процедур и вмешательств по заданным ID", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "read/procedures-and-interventions/byIdIn/{ids}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProceduresAndInterventionsIterableByIdIn(@PathVariable List<String> ids) {
        try {
            return builder(success(proceduresAndInterventionsService.readIterableByIdIn(ids)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех процедур и вмешательств по указанному списку", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "read/procedures-and-interventions/iterable/byIdIn/{selectedIds}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategorizedProceduresAndInterventionsIterableByIdIn(@PathVariable List<String> selectedIds) {
        try {
            return builder(success(proceduresAndInterventionsService.readCategorizedListByIdIn(selectedIds)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех процедур и вмешательств по категориям", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "read/categorized/procedures-and-interventions", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCategorizedProceduresAndInterventions() {
        try {
            return builder(success(proceduresAndInterventionsService.readCategorizedList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех процедур и вмешательств по фильтру", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "read/procedures-and-interventions/iterable/byCategoryId/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProceduresAndInterventionsIterableByCategoryId(@PathVariable(name = "filter") String categoryId) {
        try {
            return builder(success(proceduresAndInterventionsService.readIterableByCategoryid(categoryId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить процедур и вмешательств по заданному ID", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "read/procedures-and-interventions/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProceduresAndInterventions(@PathVariable(name = "id") String id) {
        try {
            return builder(success(proceduresAndInterventionsService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую процедур и вмешательств", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "create/procedures-and-interventions", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createProceduresAndInterventions(@RequestBody @Valid ProceduresAndInterventions proceduresAndInterventions) {
        try {
            return builder(success(proceduresAndInterventionsService.create(proceduresAndInterventions)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить существующую процедур и вмешательств", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "update/procedures-and-interventions", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateProceduresAndInterventions(@RequestBody @Valid ProceduresAndInterventions proceduresAndInterventions) {
        try {
            return builder(success(proceduresAndInterventionsService.update(proceduresAndInterventions)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить процедур и вмешательств", tags = {"PROCEDURES_AND_INTERVENTIONS"})
    @RequestMapping(value = "delete/procedures-and-interventions/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteProceduresAndInterventions(@PathVariable(name = "id") String id) {
        try {
            proceduresAndInterventionsService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    /******************************************************************************************************
     * Medicine
     */

    @ApiOperation(value = "Получить объект/список лекарств  по параметрам с пагинацией", tags = {"MEDICINE"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", dataType = "string", value = "Возвращает список объектов с данным категорий", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов Laboratory существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/medicine/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readMedicinePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(medicineService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список лекарств", tags = {"MEDICINE"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/medicine/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchMedicinePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(medicineService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лекарств", tags = {"MEDICINE"})
    @RequestMapping(value = "read/medicine/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMedicineIterable() {
        try {
            return builder(success(medicineService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лекарств для выбранных ID", tags = {"MEDICINE"})
    @RequestMapping(value = "read/medicine/iterable/byIdIn/{selectedIds}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMedicineIterableByIdIn(@PathVariable List<String> selectedIds) {
        try {
            return builder(success(medicineService.readIterableByIdIn(selectedIds)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех лекарств по фильтру", tags = {"MEDICINE"})
    @RequestMapping(value = "read/medicine/iterable/byCategoryId/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMedicineIterableByCategoryId(@PathVariable(name = "filter") String categoryId) {
        try {
            return builder(success(medicineService.readIterableByCategoryId(categoryId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить лекарств по заданному ID", tags = {"MEDICINE"})
    @RequestMapping(value = "read/medicine/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMedicine(@PathVariable(name = "id") String id) {
        try {
            return builder(success(medicineService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую лекарств", tags = {"MEDICINE"})
    @RequestMapping(value = "create/medicine", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createMedicine(@RequestBody @Valid Medicine medicine) {
        try {
            return builder(success(medicineService.create(medicine)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить существующую лекарств", tags = {"MEDICINE"})
    @RequestMapping(value = "update/medicine", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateLaboratory(@RequestBody @Valid Medicine medicine) {
        try {
            return builder(success(medicineService.update(medicine)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить лекарству", tags = {"MEDICINE"})
    @RequestMapping(value = "delete/medicine/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteMedicine(@PathVariable(name = "id") String id) {
        try {
            medicineService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * DMPV2 Services
     */

    @ApiOperation(value = "Получить объект/список DMPV2 по параметрам", tags = {"DMPV2"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "nameRu", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "nameEn", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "nameKz", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPV2 существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmpv2/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPV2Pageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpV2Service.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPV2", tags = {"DMPV2"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmpv2/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPV2Pageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpV2Service.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPV2", tags = {"DMPV2"})
    @RequestMapping(value = "/read/dmpv2/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPV2Iterable() {
        try {
            return builder(success(dmpV2Service.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPV2 по заданному ID", tags = {"DMPV2"})
    @RequestMapping(value = "/read/dmpv2/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPV2ById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpV2Service.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать объект DMPV2", tags = {"DMPV2"})
    @RequestMapping(value = "/create/dmpv2", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPV2(@RequestBody @Valid DMPV2 value) {
        try {
            return builder(success(dmpV2Service.create(value)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPV2", tags = {"DMPV2"})
    @RequestMapping(value = "/update/dmpv2/disease/selection", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPV2AfterDiseaseSelection(@RequestBody @Valid DMPV2 value) {
        try {
            return builder(success(dmpV2Service.updateAfterDiseaseSelection(value)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPV2", tags = {"DMPV2"})
    @RequestMapping(value = "/update/dmpv2", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPV2(@RequestBody @Valid DMPV2 value) {
        try {
            return builder(success(dmpV2Service.update(value)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Remove doc by id", tags = {"DMPV2"})
    @RequestMapping(value = "/delete/dmpv2/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPV2(@PathVariable(name = "id") String id) {
        try {
            dmpV2Service.delete(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    /******************************************************************************************************
     * Protocol Services
     */

    @ApiOperation(value = "Получить объект/список протокол по параметрам с пагинацией", tags = {"PROTOCOL"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "diseaseId", dataType = "string", value = "Возвращает список объектов с данным diseaseId", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов Protocol существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/protocol/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readProtocolPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(protocolService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список протокол", tags = {"PROTOCOL"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Protocol существуют и возвращает.")
    })
    @RequestMapping(value = "/search/protocol/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchProtocolPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(protocolService.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех протокол", tags = {"PROTOCOL"})
    @RequestMapping(value = "read/protocol/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProtocolIterable() {
        try {
            return builder(success(protocolService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех протокол по фильтру", tags = {"PROTOCOL"})
    @RequestMapping(value = "read/protocol/iterable/byDMPV2Id/{filter}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProtocolIterableByDMPV2Id(@PathVariable(name = "filter") String dmpV2Id) {
        try {
            return builder(success(protocolService.readIterableByDMPV2Id(dmpV2Id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить протокол по заданному ID", tags = {"PROTOCOL"})
    @RequestMapping(value = "read/protocol/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProtocol(@PathVariable(name = "id") String id) {
        try {
            return builder(success(protocolService.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать новую протокол", tags = {"PROTOCOL"})
    @RequestMapping(value = "create/protocol", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createProtocol(@RequestBody @Valid Protocol protocol) {
        try {
            return builder(success(protocolService.create(protocol)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить существующую протокол", tags = {"PROTOCOL"})
    @RequestMapping(value = "update/protocol", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateProtocol(@RequestBody @Valid Protocol protocol) {
        try {
            return builder(success(protocolService.update(protocol)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Add doc", tags = {"PROTOCOL"}, consumes = "multipart/form-data")
    @RequestMapping(value = "upload/protocol/file", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, InternalException {

        try {

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));
            }

            String mimeType = file.getContentType();
            String fileName = file.getName();
            String fileId = protocolService.saveFile(file.getInputStream(), mimeType, fileName);
            LOGGER.debug("[uploadFile()] - file name : " + fileName);
            LOGGER.debug("[uploadFile()] - file id: " + fileId);

            return builder(success(fileId));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить  file protocol", tags = {"PROTOCOL"},
            notes = "")
    @RequestMapping(value = "download/protocol/file/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getProtocolFileById(
            @ApiParam(name = "fileId")
            @PathVariable String fileId,
            HttpServletRequest request, HttpServletResponse response) throws InternalException, IOException {
        try {

            GridFsResource resource = protocolService.downloadFile(fileId);

            if (resource == null) {
                throw new CloudocObjectNotFoundException("File с идентификатором '"
                        + fileId + "' не имеет file.", resource.getClass());
            }

            String contentType = resource.getContentType();

            HttpHeaders responseHeaders = new HttpHeaders();

            responseHeaders.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Удалить протокол", tags = {"PROTOCOL"})
    @RequestMapping(value = "delete/protocol/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteProtocol(@PathVariable(name = "id") String id) {
        try {
            protocolService.delete(id);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
