package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.service.IDMPDeviceStatConfigService;
import kz.beeset.med.dmp.service.IDMPDeviceStatService;
import kz.beeset.med.dmp.service.IDMPInvitationService;
import kz.beeset.med.dmp.service.IDMPPatientService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dmp/patients")
@Api(tags = {"DMPPatients"}, description = "DMPPatients", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPPatientsController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPPatientsController.class);

    @Autowired
    private IDMPPatientService dmpPatientService;
    @Autowired
    private IDMPDeviceStatService dmpDeviceStatService;
    @Autowired
    private IDMPDeviceStatConfigService dmpDeviceStatConfigService;
    @Autowired
    private IDMPInvitationService dmpInvitationService;

    @ApiOperation(value = "Получить объект/список DMPPatients по параметрам", tags = {"DMPPatients"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "codeNumber", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "healthStatus", dataType = "string", value = "Возвращает список объектов с данным статусом", paramType = "query"),
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

    @ApiOperation(value = "Получить список DMPPatients", tags = {"DMPPatients"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "healthStatus", dataType = "string", value = "Возвращает список объектов с данным статусом", paramType = "query"),
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

    @ApiOperation(value = "Получить список всех DMPPatient", tags = {"DMPPatients"})
    @RequestMapping(value = "read/dmp/patient/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatientIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpPatientService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPPatient по dmpId и dmpDoctorUserId", tags = {"DMPPatients"})
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

    @ApiOperation(value = "Получить DMPPatient по заданному ID", tags = {"DMPPatients"})
    @RequestMapping(value = "read/dmp/patient/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatient(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpPatientService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPPatient по заданному userID", tags = {"DMPPatients"})
    @RequestMapping(value = "read/dmp/patients/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatientsByUserId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpPatientService.getAllByUserId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPPatient по заданному List<String> dmpDoctorIds", tags = {"DMPPatients"})
    @RequestMapping(value = "read/dmp/patients/byDoctorUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatientsByDmpDoctorIds(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(dmpPatientService.getAllByDoctorUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPPatient", tags = {"DMPPatients"})
    @RequestMapping(value = "create/dmp/patient", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPPatient(@RequestBody @Valid DMPPatient dmpPatient) {
        try {
            return builder(success(dmpPatientService.create(dmpPatient)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPPatient", tags = {"DMPPatients"})
    @RequestMapping(value = "create/dmp/patient/temp/{patientIdn}/{doctorCodeNumber}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPPatientTemp(@PathVariable(name = "patientIdn") String patientIdn,
                                                  @PathVariable(name = "doctorCodeNumber") String doctorCodeNumber,
                                                  @RequestBody @Valid DMPPatient dmpPatient) {
        try {
            return builder(success(dmpPatientService.createPatientTemp(patientIdn, doctorCodeNumber, dmpPatient)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPPatient", tags = {"DMPPatients"})
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

    @ApiOperation(value = "Получить объект/список DMPDeviceStat по параметрам", tags = {"DMPDeviceStat"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "checkDate", dataType = "string", value = "Возвращает список объектов с данной датой", paramType = "query"),
            @ApiImplicitParam(name = "deviceInfo", dataType = "string", value = "Возвращает список объектов с данной инфорамцией", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPPatient существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/deviceStat/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPDeviceStatPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDeviceStatService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPDeviceStat", tags = {"DMPDeviceStat"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/deviceStat/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPDeviceStatPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDeviceStatService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPDeviceStat", tags = {"DMDMPDeviceStatPPatients"})
    @RequestMapping(value = "read/dmp/deviceStat/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDeviceStatIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpDeviceStatService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPDeviceStat по заданному ID", tags = {"DMPDeviceStat"})
    @RequestMapping(value = "read/dmp/deviceStat/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDeviceStat(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDeviceStatService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPDeviceStat по заданному dmpPatientId", tags = {"DMPDeviceStat"})
    @RequestMapping(value = "read/last/dmp/deviceStat/{dmpPatientId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLastDMPDeviceStatRecord(@PathVariable(name = "dmpPatientId") String dmpPatientId) {
        try {
            return builder(success(dmpDeviceStatService.getLastRecordByDMPPatientId(dmpPatientId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPDeviceStat", tags = {"DMPDeviceStat"})
    @RequestMapping(value = "create/dmp/deviceStat", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPDeviceStat(@RequestBody @Valid DMPDeviceStat dmpDeviceStat) {
        try {
            return builder(success(dmpDeviceStatService.create(dmpDeviceStat)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPDeviceStat", tags = {"DMPDeviceStat"})
    @RequestMapping(value = "update/dmp/deviceStat", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPDeviceStat(@RequestBody @Valid DMPDeviceStat dmpDeviceStat) {
        try {
            return builder(success(dmpDeviceStatService.update(dmpDeviceStat)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPDeviceStat", tags = {"DMPDeviceStat"})
    @RequestMapping(value = "delete/dmp/deviceStat/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPDeviceStat(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDeviceStatService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPDeviceStatConfig по параметрам", tags = {"DMPDeviceStatConfig"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPDeviceStatConfig существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/deviceStatConfig/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPDeviceStatConfigPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpDeviceStatConfigService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPDeviceStatConfig", tags = {"DMPDeviceStatConfig"})
    @RequestMapping(value = "read/dmp/deviceStatConfig/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDeviceStatConfigIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpDeviceStatConfigService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPDeviceStatConfig по заданному ID", tags = {"DMPDeviceStatConfig"})
    @RequestMapping(value = "read/dmp/deviceStatConfig/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDeviceStatConfig(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDeviceStatConfigService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPDeviceStatConfig", tags = {"DMPDeviceStatConfig"})
    @RequestMapping(value = "create/dmp/deviceStatConfig", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPDeviceStatConfig(@RequestBody @Valid DMPDeviceStatConfig dmpDeviceStatConfig) {
        try {
            return builder(success(dmpDeviceStatConfigService.create(dmpDeviceStatConfig)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPDeviceStatConfig", tags = {"DMPDeviceStatConfig"})
    @RequestMapping(value = "update/dmp/deviceStatConfig", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPDeviceStatConfig(@RequestBody @Valid DMPDeviceStatConfig dmpDeviceStatConfig) {
        try {
            return builder(success(dmpDeviceStatConfigService.update(dmpDeviceStatConfig)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPDeviceStatConfig", tags = {"DMPDeviceStatConfig"})
    @RequestMapping(value = "delete/dmp/deviceStatConfig/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPDeviceStatConfig(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpDeviceStatConfigService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPInvitation по параметрам", tags = {"DMPInvitation"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPInvitation существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/invitation/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPInvitationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpInvitationService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPInvitation", tags = {"DMPInvitation"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "DMP ID", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "DMP DOCTOR ID", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID врача", paramType = "query"),
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/invitation/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPInvitationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpInvitationService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPInvitation по параметрам", tags = {"DMPInvitation"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID врача", paramType = "query"),
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "message", dataType = "string", value = "Возвращает список объектов с данным сообщением", paramType = "query"),
            @ApiImplicitParam(name = "invitationStatus", dataType = "string", value = "Возвращает список объектов с данным статусом", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPInvitation существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/invitation/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPInvitationCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpInvitationService.readCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPInvitation", tags = {"DMPInvitation"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "DMP ID", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "DMP DOCTOR ID", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID врача", paramType = "query"),
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным пользовательским ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/invitation/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPInvitationCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpInvitationService.searchCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPInvitation", tags = {"DMPInvitation"})
    @RequestMapping(value = "read/dmp/invitation/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPInvitationIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpInvitationService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPInvitation по заданному ID", tags = {"DMPInvitation"})
    @RequestMapping(value = "read/dmp/invitation/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPInvitation(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpInvitationService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPInvitation", tags = {"DMPInvitation"})
    @RequestMapping(value = "create/dmp/invitation", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPInvitation(@RequestBody @Valid DMPInvitation dmpInvitation) {
        try {
            return builder(success(dmpInvitationService.create(dmpInvitation)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPInvitation", tags = {"DMPInvitation"})
    @RequestMapping(value = "update/dmp/invitation", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPInvitation(@RequestBody @Valid DMPInvitation dmpInvitation) {
        try {
            return builder(success(dmpInvitationService.update(dmpInvitation)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPInvitation", tags = {"DMPInvitation"})
    @RequestMapping(value = "delete/dmp/invitation/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPInvitation(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpInvitationService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
