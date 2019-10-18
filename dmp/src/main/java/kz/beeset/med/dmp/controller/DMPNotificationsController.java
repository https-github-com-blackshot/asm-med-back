package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.DMPVisit;
import kz.beeset.med.dmp.service.IDMPNotificationService;
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
@RequestMapping("dmp/notification")
@Api(tags = {"DMPNotifications"}, description = "DMPNotifications", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPNotificationsController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPNotificationsController.class);

    @Autowired
    private IDMPNotificationService dmpNotificationService;

    @ApiOperation(value = "Получить объект/список DMPNotifications по параметрам", tags = {"DMPNotifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "type", dataType = "string", value = "Возвращает список объектов с данным типом", paramType = "query"),
            @ApiImplicitParam(name = "message", dataType = "string", value = "Возвращает список объектов с данным сообщением", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", dataType = "string", value = "Возвращает список объектов с данной датой начала", paramType = "query"),
            @ApiImplicitParam(name = "endDate", dataType = "string", value = "Возвращает список объектов с данной датой окончания", paramType = "query"),
            @ApiImplicitParam(name = "period", dataType = "string", value = "Возвращает список объектов с данным периодом", paramType = "query"),
            @ApiImplicitParam(name = "eventTime", dataType = "string", value = "Возвращает список объектов с данным временем", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPNotifications существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/notification/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPNotificationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpNotificationService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPNotifications", tags = {"DMPNotifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/notification/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPNotificationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpNotificationService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPNotificationCustom по параметрам", tags = {"DMPNotifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "type", dataType = "string", value = "Возвращает список объектов с данным типом", paramType = "query"),
            @ApiImplicitParam(name = "message", dataType = "string", value = "Возвращает список объектов с данным сообщением", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", dataType = "string", value = "Возвращает список объектов с данной датой начала", paramType = "query"),
            @ApiImplicitParam(name = "endDate", dataType = "string", value = "Возвращает список объектов с данной датой окончания", paramType = "query"),
            @ApiImplicitParam(name = "period", dataType = "string", value = "Возвращает список объектов с данным периодом", paramType = "query"),
            @ApiImplicitParam(name = "eventTime", dataType = "string", value = "Возвращает список объектов с данным временем", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPNotifications существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/notification/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPNotificationCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpNotificationService.readCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPNotificationCustom", tags = {"DMPNotifications"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorId", dataType = "string", value = "Возвращает список объектов с данным ID врача", paramType = "query"),
            @ApiImplicitParam(name = "dmpDoctorUserId", dataType = "string", value = "Возвращает список объектов с данным ID пользователя", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/notification/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPNotificationCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpNotificationService.searchCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPNotifications", tags = {"DMPNotifications"})
    @RequestMapping(value = "read/dmp/notification/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPNotificationIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpNotificationService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPNotifications по заданному userID", tags = {"DMPNotifications"})
    @RequestMapping(value = "read/dmp/notifications/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPNotificationsByUserId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpNotificationService.getAllByUserId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPNotifications по заданному ID", tags = {"DMPNotifications"})
    @RequestMapping(value = "read/dmp/notification/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPNotification(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpNotificationService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPNotifications", tags = {"DMPNotifications"})
    @RequestMapping(value = "create/dmp/notification", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPNotification(@RequestBody @Valid DMPNotification dmpNotifications) {
        try {
            return builder(success(dmpNotificationService.create(dmpNotifications)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPNotifications", tags = {"DMPNotifications"})
    @RequestMapping(value = "update/dmp/notification", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPNotification(@RequestBody @Valid DMPNotification dmpNotifications) {
        try {
            return builder(success(dmpNotificationService.update(dmpNotifications)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPNotifications", tags = {"DMPNotifications"})
    @RequestMapping(value = "delete/dmp/notification/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPNotification(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpNotificationService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
