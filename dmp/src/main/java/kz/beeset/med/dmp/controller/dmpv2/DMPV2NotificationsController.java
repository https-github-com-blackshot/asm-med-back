package kz.beeset.med.dmp.controller.dmpv2;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Notifications;
import kz.beeset.med.dmp.service.dmpv2.IDMPV2NotificationsService;
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
@RequestMapping("dmpv2/notifications")
@Api(tags = {"DMPV2Notifications"}, description = "DMPV2Notifications", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPV2NotificationsController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2NotificationsController.class);

    @Autowired
    private IDMPV2NotificationsService notificationsService;

    @ApiOperation(value = "Получить объект/список DMPV2Notifications по параметрам", tags = {"DMPV2Notifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "patients", dataType = "string", value = "Возвращает список объектов с данным ID пациентов", paramType = "query"),
            @ApiImplicitParam(name = "type", dataType = "string", value = "Возвращает список объектов с данным типом", paramType = "query"),
            @ApiImplicitParam(name = "message", dataType = "string", value = "Возвращает список объектов с данным сообщением", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", dataType = "string", value = "Возвращает список объектов с данной датой начала", paramType = "query"),
            @ApiImplicitParam(name = "endDate", dataType = "string", value = "Возвращает список объектов с данной датой окончания", paramType = "query"),
            @ApiImplicitParam(name = "period", dataType = "string", value = "Возвращает список объектов с данным периодом", paramType = "query"),
            @ApiImplicitParam(name = "eventTime", dataType = "string", value = "Возвращает список объектов с данным временем", paramType = "query"),
            @ApiImplicitParam(name = "toAll", dataType = "string", value = "Возвращает список объектов с данным значением", paramType = "query"),
            @ApiImplicitParam(name = "profileId", dataType = "string", value = "Возвращает список объектов с данным ID профиля пациента", paramType = "query"),
            @ApiImplicitParam(name = "doctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPNotifications существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getNotificationsPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(notificationsService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPV2Notifications", tags = {"DMPV2Notifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "doctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "profileId", dataType = "string", value = "Возвращает список объектов с данным ID профиля пациента", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchNotificationsPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(notificationsService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPV2Notifications по параметрам", tags = {"DMPV2Notifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "patients", dataType = "string", value = "Возвращает список объектов с данным ID пациентов", paramType = "query"),
            @ApiImplicitParam(name = "type", dataType = "string", value = "Возвращает список объектов с данным типом", paramType = "query"),
            @ApiImplicitParam(name = "message", dataType = "string", value = "Возвращает список объектов с данным сообщением", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", dataType = "string", value = "Возвращает список объектов с данной датой начала", paramType = "query"),
            @ApiImplicitParam(name = "endDate", dataType = "string", value = "Возвращает список объектов с данной датой окончания", paramType = "query"),
            @ApiImplicitParam(name = "period", dataType = "string", value = "Возвращает список объектов с данным периодом", paramType = "query"),
            @ApiImplicitParam(name = "eventTime", dataType = "string", value = "Возвращает список объектов с данным временем", paramType = "query"),
            @ApiImplicitParam(name = "toAll", dataType = "string", value = "Возвращает список объектов с данным значением", paramType = "query"),
            @ApiImplicitParam(name = "profileId", dataType = "string", value = "Возвращает список объектов с данным ID профиля пациента", paramType = "query"),
            @ApiImplicitParam(name = "doctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPNotifications существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getNotificationsCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(notificationsService.readCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPV2Notifications", tags = {"DMPV2Notifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientUserId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "doctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "profileId", dataType = "string", value = "Возвращает список объектов с данным ID профиля пациента", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchNotificationsCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(notificationsService.searchCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPV2Notifications по ID профиля пациента", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "read/iterable/byProfileId/{profileId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getNotificationsIterableByProfileId(@PathVariable(name = "profileId") String profileId) {
        try {
            return builder(success(notificationsService.readIterableByProfileId(profileId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPV2Notifications по ID профиля пациента", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "read/iterable/byPatientUserId/{patientUserId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getNotificationsIterableByPatientUserId(@PathVariable(name = "patientUserId") String patientUserId) {
        try {
            return builder(success(notificationsService.readIterableByPatientUserId(patientUserId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPV2Notifications", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "read/iterable/byDoctorUserIdAndPatientUserId/{doctorUserId}/{patientUserId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getNotificationsIterableByDoctorUserIdAndPatientUserId(@PathVariable(name = "doctorUserId") String doctorUserId,
                                                      @PathVariable(name = "patientUserId") String patientUserId) {
        try {
            return builder(success(notificationsService.readIterableByDoctorUserIdAndPatientUserId(doctorUserId, patientUserId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPV2Notifications по заданному ID", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "read/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getNotification(@PathVariable(name = "id") String id) {
        try {
            return builder(success(notificationsService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPV2Notifications", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createNotification(@RequestBody @Valid DMPV2Notifications notification) {
        try {
            return builder(success(notificationsService.create(notification)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPV2Notifications", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "update", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateNotification(@RequestBody @Valid DMPV2Notifications notification) {
        try {
            return builder(success(notificationsService.update(notification)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPV2Notifications", tags = {"DMPV2Notifications"})
    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPNotification(@PathVariable(name = "id") String id) {
        try {
            notificationsService.delete(id);
            return builder(success("DELETED"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
