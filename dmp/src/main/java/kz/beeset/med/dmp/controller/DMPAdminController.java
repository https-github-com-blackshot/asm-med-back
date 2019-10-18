package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPOrganizationMap;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.service.IDMPDoctorService;
import kz.beeset.med.dmp.service.IDMPOrganizationMapService;
import kz.beeset.med.dmp.service.IDMPPatientService;
import kz.beeset.med.dmp.service.IDMPService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dmp/main")
@Api(tags = {"DMP"}, description = "DMP", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPAdminController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPAdminController.class);

    @Autowired
    private IDMPService dmpService;
    @Autowired
    private IDMPPatientService dmpPatientService;
    @Autowired
    private IDMPDoctorService dmpDoctorService;
    @Autowired
    private IDMPOrganizationMapService dmpOrganizationMapService;

    /******************************************************************************************************
     * DMP Services
     */

    @ApiOperation(value = "Получить объект/список DMP по параметрам", tags = {"DMP"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "nameKz", dataType = "string", value = "Возвращает список объектов с данным название на казахском", paramType = "query"),
            @ApiImplicitParam(name = "nameRu", dataType = "string", value = "Возвращает список объектов с данным название на русском", paramType = "query"),
            @ApiImplicitParam(name = "nameEn", dataType = "string", value = "Возвращает список объектов с данным название на английском", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "registrationDate", dataType = "string", value = "Возвращает список объектов с в рамках это даты", paramType = "query"),
            @ApiImplicitParam(name = "category", dataType = "string", value = "Возвращает список объектов относящихся к этой категории", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMP существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpService.read(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMP", tags = {"DMP"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMP", tags = {"DMP"})
    @RequestMapping(value = "read/dmp/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPIterable() {
        try {
            return builder(success(dmpService.read()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMP по ID врача", tags = {"DMP"})
    @RequestMapping(value = "read/dmp/iterable/byDoctorUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPIterableByDoctorUserId(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(dmpService.readByDoctorUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMP по ID пациента", tags = {"DMP"})
    @RequestMapping(value = "read/dmp/iterable/byPatientUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPIterableByPatientByUserId(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(dmpService.readByPatientUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMP по данной категории", tags = {"DMP"})
    @RequestMapping(value = "read/dmp/iterable/byCategory/{category}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPIterableByCategory(@PathVariable(name = "category") String category) {
        try {
            return builder(success(dmpService.readByCategory(category)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMP по заданному ID", tags = {"DMP"})
    @RequestMapping(value = "read/dmp/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMP(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMP", tags = {"DMP"})
    @RequestMapping(value = "create/dmp", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMP(@RequestBody @Valid DMP dmp) {
        try {
            return builder(success(dmpService.create(dmp)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMP", tags = {"DMP"})
    @RequestMapping(value = "update/dmp", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMP(@RequestBody @Valid DMP dmp) {
        try {
            return builder(success(dmpService.update(dmp)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMP", tags = {"DMP"})
    @RequestMapping(value = "delete/dmp/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMP(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * DMP Patient Services
     */

    @ApiOperation(value = "Получить объект/список DMPPatient по параметрам", tags = {"DMPPatient"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "codeNumber", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPPatient существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/patient/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPPatientPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpPatientService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPPatient", tags = {"DMPPatient"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/patient/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPPatientPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpPatientService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPPatient", tags = {"DMPPatient"})
    @RequestMapping(value = "read/dmp/patient/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatientIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpPatientService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPPatient по dmpId и dmpDoctorUserId", tags = {"DMPPatient"})
    @RequestMapping(value = "read/dmp/patient/iterable/{dmpId}/{dmpDoctorUserId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatientIterable(@PathVariable(name = "dmpId") String dmpId,
                                                   @PathVariable(name = "dmpDoctorUserId") String dmpDoctorUserId) {
        try {
            return builder(success(dmpPatientService.readIterableByDMPIdAndDMPDoctorUserId(dmpId, dmpDoctorUserId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPPatient по заданному ID", tags = {"DMPPatient"})
    @RequestMapping(value = "read/dmp/patient/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatient(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpPatientService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPPatient", tags = {"DMPPatient"})
    @RequestMapping(value = "create/dmp/patient", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPPatient(@RequestBody @Valid DMPPatient dmpPatient) {
        try {
            return builder(success(dmpPatientService.create(dmpPatient)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPPatient", tags = {"DMPPatient"})
    @RequestMapping(value = "update/dmp/patient", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPPatient(@RequestBody @Valid DMPPatient dmpPatient) {
        try {
            return builder(success(dmpPatientService.update(dmpPatient)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPPatient", tags = {"DMPPatient"})
    @RequestMapping(value = "delete/dmp/patient/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPPatient(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpPatientService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * DMP Doctor Services
     */

    @ApiOperation(value = "Получить объект/список DMPDoctor по параметрам", tags = {"DMPDoctor"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "codeNumber", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPDoctor существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/doctor/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPDoctorPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDoctorService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPDoctor", tags = {"DMPDoctor"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "dmpId с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/doctor/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPDoctorPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDoctorService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить объект/список DMPDoctor по параметрам", tags = {"DMPDoctor"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "codeNumber", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPDoctor существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/doctor/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPDoctorCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDoctorService.readCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPDoctor", tags = {"DMPDoctor"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/doctor/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPDoctorCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDoctorService.searchCustom(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPDoctor by template id", tags = {"DMPDoctor"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/doctor/list/{dmpId}/{templateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPDoctorsByTemplateId(@PathVariable("templateId") String templateId,
                                                          @PathVariable("dmpId") String dmpId) {
        try {
            return builder(success(dmpDoctorService.readIterableByTemplateId(dmpId,templateId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPDoctor", tags = {"DMPDoctor"})
    @RequestMapping(value = "read/dmp/doctor/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDoctorIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpDoctorService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPDoctor", tags = {"DMPDoctor"})
    @RequestMapping(value = "read/dmp/doctor/iterable/byIds/{dmpDoctorIds}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDoctorIterableByIdsIn(@PathVariable(name = "dmpDoctorIds") List<String> dmpDoctorIds) {
        try {
            return builder(success(dmpDoctorService.readIterableByIds(dmpDoctorIds)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPDoctor по заданному ID", tags = {"DMPDoctor"})
    @RequestMapping(value = "read/dmp/doctor/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDoctor(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDoctorService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPDoctor по заданному userID", tags = {"DMPPatients"})
    @RequestMapping(value = "read/dmp/doctors/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatientsByUserId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDoctorService.getAllByUserId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPDoctor", tags = {"DMPDoctor"})
    @RequestMapping(value = "create/dmp/doctor", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPDoctor(@RequestBody @Valid DMPDoctor dmpDoctor) {
        try {
            return builder(success(dmpDoctorService.create(dmpDoctor)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPDoctor", tags = {"DMPDoctor"})
    @RequestMapping(value = "update/dmp/doctor", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPDoctor(@RequestBody @Valid DMPDoctor dmpDoctor) {
        try {
            return builder(success(dmpDoctorService.update(dmpDoctor)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPDoctor", tags = {"DMPDoctor"})
    @RequestMapping(value = "delete/dmp/doctor/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPDoctor(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDoctorService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /******************************************************************************************************
     * DMP Organization Map Services
     */

    @ApiOperation(value = "Получить список ПУЗов относящихся к данной организации", tags = {"DMPOrganizationMap"})
    @RequestMapping(value = "read/dmp/iterable/byOrgId/{orgId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPIterableByOrgId(@PathVariable(name = "orgId") String orgId) {
        try {
            return builder(success(dmpOrganizationMapService.getDMPListByOrganizationId(orgId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ПУЗов относящихся к данной организации", tags = {"DMPOrganizationMap"})
    @RequestMapping(value = "save/dmp-org-map/{orgId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> saveDMPOrganizationMap(@PathVariable(name = "orgId") String orgId,
                                                    @Valid @RequestBody HashMap<String, Boolean> checkboxes) {
        try {
            return builder(success(dmpOrganizationMapService.save(orgId, checkboxes)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список ПУЗов относящихся к данной организации", tags = {"DMPOrganizationMap"})
    @RequestMapping(value = "delete/dmp-org-map/{orgId}/{dmpId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPOrganizationMap(@PathVariable(name = "orgId") String orgId,
                                                      @PathVariable(name = "dmpId") String dmpId) {
        try {
            dmpOrganizationMapService.delete(orgId, dmpId);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
