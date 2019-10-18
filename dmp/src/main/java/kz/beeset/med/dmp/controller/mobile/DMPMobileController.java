package kz.beeset.med.dmp.controller.mobile;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.firebase.PushNotificationModel;
import kz.beeset.med.dmp.model.DMPDeviceStat;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPInvitation;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.service.mobile.IDMPMobileService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dmp/mobile")
@Api(tags = {"DMPMobile"}, description = "DMPMobile", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPMobileController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPMobileController.class);

    @Autowired
    private IDMPMobileService mobileService;

    @ApiOperation(value = "Получить объект/список DMP по параметрам", tags = {"DMPMobile"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "code", dataType = "string", value = "Возвращает список объектов с данным кодом", paramType = "query"),
            @ApiImplicitParam(name = "nameKz", dataType = "string", value = "Возвращает список объектов с данным название на казахском", paramType = "query"),
            @ApiImplicitParam(name = "nameRu", dataType = "string", value = "Возвращает список объектов с данным название на русском", paramType = "query"),
            @ApiImplicitParam(name = "nameEn", dataType = "string", value = "Возвращает список объектов с данным название на английском", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "registrationDate", dataType = "string", value = "Возвращает список объектов с в рамках это даты", paramType = "query"),
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
            return builder(success(mobileService.readDMPPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMP по ID пользователя", tags = {"DMPMobile"})
    @RequestMapping(value = "read/dmp/iterable/byUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPIterable(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(mobileService.readDMPByUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPPatients по параметрам", tags = {"DMPMobile"})
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
            return builder(success(mobileService.readDMPPatientPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "get/dmp/patient/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPPatient(@PathVariable(name = "id") String id) {
        try {
            return builder(success(mobileService.getDMPPatientById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "register/dmp/patient", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerDMPPatient(@Valid @RequestBody DMPPatient dmpPatient) {
        try {
            return builder(success(mobileService.registerPatientToDMP(dmpPatient)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPDoctor", tags = {"DMPMobile"})
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
    @RequestMapping(value = "read/dmp/doctor/custom/pageable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDMPDoctorCustomPageable(@ApiParam(hidden = true) @RequestParam HashMap<String, String> allRequestParams) {
        try {
            return builder(success(mobileService.readDMPDoctorCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "refuse/dmp/doctor/participation/{dmpPatientId}/{dmpDoctorId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> refuseDoctorParticipation(@PathVariable(name = "dmpPatientId") String dmpPatientId,
                                                       @PathVariable(name = "dmpDoctorId") String dmpDoctorId) {
        try {
            mobileService.refuseDoctorParticipation(dmpPatientId, dmpDoctorId);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "get/dmp/doctor/{userId}/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDoctorInfo(@PathVariable(name = "userId") String userId,
                                              @PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(mobileService.getDMPDoctorInfo(userId, dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
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
    @RequestMapping(value = "read/dmp/invitation/pageabele", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDMPInvitationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(mobileService.readDMPInvitationPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
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
    @RequestMapping(value = "search/dmp/invitation", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> searchDMPInvitationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(mobileService.searchDMPInvitation(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPInvitation", tags = {"DMPMobile"})
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
    @RequestMapping(value = "read/dmp/invitation/custom/pageable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDMPInvitationCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(mobileService.readDMPInvitationCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
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
    @RequestMapping(value = "search/dmp/invitation/custom/pageable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> searchDMPInvitationCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(mobileService.searchDMPInvitationCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "read/dmp/invitation/byDmpId/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDMPInvitationIterableByDMPId(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(mobileService.readDMPInvitationIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "get/dmp/invitation/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPInvitation(@PathVariable(name = "id") String id) {
        try {
            return builder(success(mobileService.getDMPInvitation(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "create/dmp/invitation", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPInvitation(@Valid @RequestBody DMPInvitation dmpInvitation) {
        try {
            return builder(success(mobileService.createDMPInvitation(dmpInvitation)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "accept/dmp/invitation/{dmpInvitationId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> acceptDMPInvitation(@PathVariable(name = "dmpInvitationId") String dmpInvitationId) {
        try {
            return builder(success(mobileService.acceptDMPInvitation(dmpInvitationId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "reject/dmp/invitation/{dmpInvitationId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> rejectDMPInvitation(@PathVariable(name = "dmpInvitationId") String dmpInvitationId) {
        try {
            return builder(success(mobileService.rejectDMPInvitation(dmpInvitationId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "recall/dmp/invitation/{dmpInvitationId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> recallDMPInvitation(@PathVariable(name = "dmpInvitationId") String dmpInvitationId) {
        try {
            return builder(success(mobileService.recallDMPInvitation(dmpInvitationId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "register/dmp/patient/device/stat", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerDMPPatientDevice(@Valid @RequestBody DMPDeviceStat dmpDeviceStat) {
        try {
            return builder(success(mobileService.registerDMPPatientDevice(dmpDeviceStat)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "update/dmp/patient/device/stat", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> updateDeviceStat(@Valid @RequestBody DMPDeviceStat dmpDeviceStat) {
        try {
            return builder(success(mobileService.updateDeviceStat(dmpDeviceStat)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "configure/dmp/patient/device", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> configureDevice(@Valid @RequestBody DMPDeviceStatConfig dmpDeviceStatConfig) {
        try {
            return builder(success(mobileService.configureDevice(dmpDeviceStatConfig)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "get/device/configuration/{dmpPatientId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDeviceConfiguration(@PathVariable(name = "dmpPatientId") String dmpPatientId) {
        try {
            return builder(success(mobileService.getDeviceConfiguration(dmpPatientId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "send/dmp/patient/notification/{clientToken}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> sendNotificationToOneUser(@Valid @RequestBody PushNotificationModel pushNotificationModel,
                                                       @PathVariable(name = "clientToken") String clientToken) {
        try {
            return builder(success(mobileService.sendNotificationToOneUser(pushNotificationModel, clientToken)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "send/notification/toGroup/{dmpId}", method = RequestMethod.POST, produces = "application/json")
    public String sendNotificationToGroupByTopic(@Valid @RequestBody PushNotificationModel pushNotificationModel,
                                                            @PathVariable(name = "dmpId") String dmpId) {
        try {
            return mobileService.sendNotificationToGroupByTopic(pushNotificationModel, dmpId);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
//            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMobile", tags = {"DMPMobile"})
    @RequestMapping(value = "subscribe/dmp/patient/to/notification/{dmpId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> subscribeUser(@Valid @RequestBody List<String> clientTokens,
                                           @PathVariable(name = "dmpId") String dmpId) {
        try {
            mobileService.subscribeUser(dmpId, clientTokens);
            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
