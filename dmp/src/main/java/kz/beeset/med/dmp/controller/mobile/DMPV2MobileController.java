package kz.beeset.med.dmp.controller.mobile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.beeset.med.dmp.model.feign.DeviceStat;
import kz.beeset.med.dmp.model.feign.DeviceStatConfig;
import kz.beeset.med.dmp.service.mobile.IDMPV2MobileService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("dmpv2/mobile")
@Api(tags = {"DMPV2Mobile"}, description = "DMPV2Mobile", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPV2MobileController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2MobileController.class);

    @Autowired
    private IDMPV2MobileService mobileService;

    @ApiOperation(value = "Регистрация данных с устройства", tags = {"DMPV2Mobile"})
    @RequestMapping(value = "register/patient/device/stat", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerDMPPatientDevice(@Valid @RequestBody DeviceStat deviceStat) {
        try {
            return mobileService.registerPatientDevice(deviceStat);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить показания с устройства", tags = {"DMPV2Mobile"})
    @RequestMapping(value = "update/patient/device/stat", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDeviceStat(@Valid @RequestBody DeviceStat deviceStat) {
        try {
            return mobileService.updateDeviceStat(deviceStat);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Конфигурация устройства", tags = {"DMPV2Mobile"})
    @RequestMapping(value = "configure/patient/device", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> configureDevice(@Valid @RequestBody DeviceStatConfig deviceStatConfig) {
        try {
            return mobileService.configureDevice(deviceStatConfig);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить конфигурацию устройства", tags = {"DMPV2Mobile"})
    @RequestMapping(value = "get/device/configuration/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDeviceConfiguration(@PathVariable(name = "userId") String userId) {
        try {
            return mobileService.getDeviceConfiguration(userId);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
