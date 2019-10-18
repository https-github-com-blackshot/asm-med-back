package kz.beeset.med.device.controller;

import io.swagger.annotations.*;
import kz.beeset.med.device.model.DeviceStat;
import kz.beeset.med.device.model.DeviceStatConfig;
import kz.beeset.med.device.service.interfaces.IDeviceStatConfigService;
import kz.beeset.med.device.service.interfaces.IDeviceStatService;
import kz.beeset.med.device.utils.CommonService;
import kz.beeset.med.device.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("device")
@Api(tags = {"Device"}, description = "Device", authorizations = {@Authorization(value = "bearerAuth")})
public class DeviceController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private IDeviceStatService deviceStatService;
    @Autowired
    private IDeviceStatConfigService deviceStatConfigService;

    /********************************************************************************************************************************************
     *
     *      Device Stat Services
     *
     */

    @ApiOperation(value = "Получить объект/список DeviceStat по параметрам", tags = {"DeviceStat"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным UserId", paramType = "query"),
            @ApiImplicitParam(name = "checkDate", dataType = "string", value = "Возвращает список объектов с данной датой", paramType = "query"),
            @ApiImplicitParam(name = "deviceName", dataType = "string", value = "Возвращает список объектов с данной инфорамцией", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DeviceStat существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/deviceStat/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readDeviceStatPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(deviceStatService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DeviceStat", tags = {"DeviceStat"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/deviceStat/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDeviceStatPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(deviceStatService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DeviceStat", tags = {"DeviceStat"})
    @RequestMapping(value = "read/deviceStat/iterable/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDeviceStatIterable(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(deviceStatService.readIterableByUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DeviceStat по заданному ID", tags = {"DeviceStat"})
    @RequestMapping(value = "read/deviceStat/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDeviceStat(@PathVariable(name = "id") String id) {
        try {
            return builder(success(deviceStatService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DeviceStat по заданному userId", tags = {"DeviceStat"})
    @RequestMapping(value = "read/last/deviceStat/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readLastDeviceStatRecord(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(deviceStatService.getLastRecordByUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DeviceStat", tags = {"DeviceStat"})
    @RequestMapping(value = "create/deviceStat", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDeviceStat(@RequestBody @Valid DeviceStat deviceStat) {
        try {
            return builder(success(deviceStatService.create(deviceStat)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DeviceStat", tags = {"DeviceStat"})
    @RequestMapping(value = "update/deviceStat", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDeviceStat(@RequestBody @Valid DeviceStat deviceStat) {
        try {
            return builder(success(deviceStatService.update(deviceStat)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DeviceStat", tags = {"DeviceStat"})
    @RequestMapping(value = "delete/deviceStat/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDeviceStat(@PathVariable(name = "id") String id) {
        try {
            return builder(success(deviceStatService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    /********************************************************************************************************************************************
     *
     *      Device Stat Config Services
     *
     */

    @ApiOperation(value = "Получить объект/список DeviceStatConfig по параметрам", tags = {"DeviceStatConfig"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает объект с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект DeviceStatConfig существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/deviceStatConfig/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readDeviceStatConfigPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(deviceStatConfigService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DeviceStatConfig", tags = {"DeviceStatConfig"})
    @RequestMapping(value = "read/deviceStatConfig/byUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDeviceStatConfigIterable(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(deviceStatConfigService.readByUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DeviceStatConfig по заданному ID", tags = {"DeviceStatConfig"})
    @RequestMapping(value = "read/deviceStatConfig/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> readDeviceStatConfig(@PathVariable(name = "id") String id) {
        try {
            return builder(success(deviceStatConfigService.read(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DeviceStatConfig", tags = {"DeviceStatConfig"})
    @RequestMapping(value = "create/deviceStatConfig", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDeviceStatConfig(@RequestBody @Valid DeviceStatConfig deviceStatConfig) {
        try {
            return builder(success(deviceStatConfigService.create(deviceStatConfig)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DeviceStatConfig", tags = {"DeviceStatConfig"})
    @RequestMapping(value = "update/deviceStatConfig", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDeviceStatConfig(@RequestBody @Valid DeviceStatConfig deviceStatConfig) {
        try {
            return builder(success(deviceStatConfigService.update(deviceStatConfig)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DeviceStatConfig", tags = {"DeviceStatConfig"})
    @RequestMapping(value = "delete/deviceStatConfig/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDeviceStatConfig(@PathVariable(name = "id") String id) {
        try {
            return builder(success(deviceStatConfigService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
