package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMPScheduleType;
import kz.beeset.med.dmp.model.DMPVisit;
import kz.beeset.med.dmp.service.IDMPScheduleTypeService;
import kz.beeset.med.dmp.service.IDMPVisitService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dmp/visit")
@Api(tags = {"DMPVisit"}, description = "DMPVisit", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPAdminVisitController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPAdminVisitController.class);

    @Autowired
    private IDMPVisitService dmpVisitService;
    @Autowired
    private IDMPScheduleTypeService dmpScheduleTypeService;

    @ApiOperation(value = "Получить объект/список DMPVisit по параметрам", tags = {"DMPVisit"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "nameKz", dataType = "string", value = "Возвращает список объектов с данным название на казахском", paramType = "query"),
            @ApiImplicitParam(name = "nameRu", dataType = "string", value = "Возвращает список объектов с данным название на русском", paramType = "query"),
            @ApiImplicitParam(name = "nameEn", dataType = "string", value = "Возвращает список объектов с данным название на английском", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "dmpScheduleTypeId", dataType = "string", value = "Возвращает список объектов с данным ID типа расписания", paramType = "query"),
            @ApiImplicitParam(name = "days", dataType = "string", value = "Возвращает список объектов с данный количетсвом дней", paramType = "query"),
            @ApiImplicitParam(name = "deadlineOffset", dataType = "string", value = "Возвращает список объектов с данным количеством дней до дедлайна", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPVisit существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/visit/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPVisitPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpVisitService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPVisit", tags = {"DMPVisit"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/visit/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPVisitPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpVisitService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPVisit", tags = {"DMPVisit"})
    @RequestMapping(value = "read/dmp/visit/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPVisitIterable(@RequestParam(name = "dmpId", required = false) String dmpId,
                                                 @RequestParam(name = "dmpScheduleTypeId", required = false) String dmpScheduleTypeId) {
        try {
            List<DMPVisit> dmpVisitList = new ArrayList<>();
            if(dmpId != null && !dmpId.isEmpty()){
                dmpVisitList = dmpVisitService.readIterableByDMPId(dmpId);
            } else if(dmpScheduleTypeId != null && !dmpScheduleTypeId.isEmpty()){
                dmpVisitList = dmpVisitService.readIterableByDMPScheduleTypeId(dmpScheduleTypeId);
            }
            return builder(success(dmpVisitList));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех Form", tags = {"DMPVisit"})
    @RequestMapping(value = "read/dmp/visit/forms/{visitId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPVisitFormsIterable(@PathVariable(name = "visitId") String visitId) {
        try {
            return builder(success(dmpVisitService.readFormsByVisitId(visitId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список Form", tags = {"DMPVisit"})
    @RequestMapping(value = "read/dmp/visit/forms/custom/{visitId}/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCustomFormsByVisitAndUserId(@PathVariable(name = "visitId") String visitId,
                                                            @PathVariable(name = "userId") String userId) {
        try {
            return builder(success(dmpVisitService.getFormsSingleTypeVisitWHStatus(visitId, userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список Warehouse", tags = {"DMPVisit"})
    @RequestMapping(value = "read/dmp/visit/warehouse/list/{visitId}/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getWarehouseListByVisitAndUserId(@PathVariable(name = "visitId") String visitId,
                                                              @PathVariable(name = "userId") String userId) {
        try {
            return builder(success(dmpVisitService.getWarehouseListByVisitIdAndUserId(visitId, userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPVisit по заданному ID", tags = {"DMPVisit"})
    @RequestMapping(value = "read/dmp/visit/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPVisit(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpVisitService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPVisit", tags = {"DMPVisit"})
    @RequestMapping(value = "create/dmp/visit", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPVisit(@RequestBody @Valid DMPVisit dmpVisit) {
        try {
            return builder(success(dmpVisitService.create(dmpVisit)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPVisit", tags = {"DMPVisit"})
    @RequestMapping(value = "update/dmp/visit", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPVisit(@RequestBody @Valid DMPVisit dmpVisit) {
        try {
            return builder(success(dmpVisitService.update(dmpVisit)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать дубликат визита", tags = {"Visits"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Указывает, что запись была создана."),
            @ApiResponse(code = 400, message = "Указывает, что произошла ошибка при создании дубликата.")
    })
    @RequestMapping(value = "/duplicate/dmp/visit/{visitId}/{code}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createDuplicate(@ApiParam(name = "id", value = "Идентификатор визита для дубликата.") @PathVariable String visitId,
                                             @ApiParam(name = "code", value = "Код визита") @PathVariable(name = "code") String code) {
        try {
            return builder(success(dmpVisitService.duplicateDmpVisit(visitId, code)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Обновить объект DMPVisit", tags = {"DMPVisit"})
    @RequestMapping(value = "delete/dmp/visit/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPVisit(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpVisitService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPScheduleType по параметрам", tags = {"DMPScheduleType"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "nameKz", dataType = "string", value = "Возвращает список объектов с данным название на казахском", paramType = "query"),
            @ApiImplicitParam(name = "nameRu", dataType = "string", value = "Возвращает список объектов с данным название на русском", paramType = "query"),
            @ApiImplicitParam(name = "nameEn", dataType = "string", value = "Возвращает список объектов с данным название на английском", paramType = "query"),
            @ApiImplicitParam(name = "type", dataType = "string", value = "Возвращает список объектов с данным типом", paramType = "query"),
            @ApiImplicitParam(name = "visitCount", dataType = "string", value = "Возвращает список объектов с данным количеством визитов", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPScheduleType существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/scheduleType/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPScheduleTypePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpScheduleTypeService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPScheduleType", tags = {"DMPScheduleType"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "DMP Id", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/scheduleType/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPScheduleTypePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpScheduleTypeService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPScheduleType", tags = {"DMPScheduleType"})
    @RequestMapping(value = "read/dmp/scheduleType/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPScheduleTypeIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpScheduleTypeService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }



    @ApiOperation(value = "Получить список подтипов типов расписаний", tags = {"DMPScheduleType"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/read/dmp/scheduleType/subType/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readScheduleTypeSubTypeList() {
        try {
            return builder(success(dmpScheduleTypeService.getDMPScheduleTypeSubTypes()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить подтип типов расписаний по коду", tags = {"DMPScheduleType"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/read/dmp/scheduleType/subType/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getScheduleTypeSubTypeByCode(@PathVariable(name = "code") String code) {
        try {
            return builder(success(dmpScheduleTypeService.getDMPScheduleTypeSubTypeByCode(code)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPScheduleType по заданному ID", tags = {"DMPScheduleType"})
    @RequestMapping(value = "read/dmp/scheduleType/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPScheduleType(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpScheduleTypeService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPScheduleType", tags = {"DMPScheduleType"})
    @RequestMapping(value = "create/dmp/scheduleType", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPScheduleType(@RequestBody @Valid DMPScheduleType dmpScheduleType) {
        try {
            return builder(success(dmpScheduleTypeService.create(dmpScheduleType)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPScheduleType", tags = {"DMPScheduleType"})
    @RequestMapping(value = "update/dmp/scheduleType", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPScheduleType(@RequestBody @Valid DMPScheduleType dmpScheduleType) {
        try {
            return builder(success(dmpScheduleTypeService.update(dmpScheduleType)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPScheduleType", tags = {"DMPScheduleType"})
    @RequestMapping(value = "delete/dmp/scheduleType/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPScheduleType(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpScheduleTypeService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список warehouse по visitId, userId, formId ПУЗа", tags = {"WAREHOUSE"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/getWarehouse/{visitId}/{userId}/{formId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable(name = "visitId") String visitId,
                                 @PathVariable(name = "userId") String userId,
                                 @PathVariable(name = "formId") String formId) {
        try {
            return builder(success(dmpVisitService.getWareHouseByVisitIdAndUserIdAndFormId(visitId, userId, formId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/get/codeAndDmpId/{code}/{dmpId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getVisitByCodeAndKiID(@PathVariable(name = "code") String code,
                                                   @PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpVisitService.getByCodeAndDMPId(code, dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список визитов по id клинического усследования", tags = {"FORMS"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/get/forms/{dmpId}/{scheduleTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFormsByDMPIdAndScheduleTypeId(@PathVariable(name = "dmpId") String dmpId,
                                                             @PathVariable(name = "scheduleTypeId") String scheduleTypeId) {
        try {
            return builder(success(dmpVisitService.getAllFormsByDMPIdAndScheduleTypeId(dmpId, scheduleTypeId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
