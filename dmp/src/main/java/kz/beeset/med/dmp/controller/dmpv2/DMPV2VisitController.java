package kz.beeset.med.dmp.controller.dmpv2;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.controller.DMPMDTController;
import kz.beeset.med.dmp.model.DMPMDTTemplate;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Visit;
import kz.beeset.med.dmp.service.dmpv2.IDMPV2VisitService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("dmpv2/visit")
@Api(tags = {"DMPV2Visit"}, description = "DMPV2Visit", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPV2VisitController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2VisitController.class);

    @Autowired
    private IDMPV2VisitService service;

    @ApiOperation(value = "Получить объект/список DMPV2Visit по параметрам", tags = {"DMPV2Visit"})
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
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPV2Visit существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/visit/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPV2VisitPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(service.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPV2Visit", tags = {"DMPV2Visit"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/visit/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPV2VisitPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(service.searchPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPV2Visit", tags = {"DMPV2Visit"})
    @RequestMapping(value = "read/visit/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPV2VisitIterable() {
        try {
            return builder(success(service.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить последний визит по заданному Patient ID", tags = {"DMPV2Visit"})
    @RequestMapping(value = "read/last/visit/{patientId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLastDMPV2Visit(@PathVariable(name = "patientId") String patientId) {
        try {
            return builder(success(service.readLastVisit(patientId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить все визиты по заданному Patient ID", tags = {"DMPV2Visit"})
    @RequestMapping(value = "read/all/visit/{patientId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllDMPV2Visit(@PathVariable(name = "patientId") String patientId) {
        try {
            return builder(success(service.readIterableByPatientId(patientId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPV2Visit по заданному ID", tags = {"DMPV2Visit"})
    @RequestMapping(value = "read/visit/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPV2Visit(@PathVariable(name = "id") String id) {
        try {
            return builder(success(service.readOne(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать объект DMPV2Visit", tags = {"DMPV2Visit"})
    @RequestMapping(value = "create/visit", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPV2Visit(@RequestBody @Valid DMPV2Visit value) {
        try {
            return builder(success(service.create(value)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPV2Visit", tags = {"DMPV2Visit"})
    @RequestMapping(value = "update/visit", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPV2Visit(@RequestBody @Valid DMPV2Visit value) {
        try {
            return builder(success(service.update(value)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Remove doc by id", tags = {"DMPV2Visit"})
    @RequestMapping(value = "/delete/visit/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPV2Visit(@PathVariable(name = "id") String id) {
        try {
            service.delete(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
