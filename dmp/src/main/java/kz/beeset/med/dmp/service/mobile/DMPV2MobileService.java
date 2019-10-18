package kz.beeset.med.dmp.service.mobile;

import kz.beeset.med.dmp.model.feign.DeviceStat;
import kz.beeset.med.dmp.model.feign.DeviceStatConfig;
import kz.beeset.med.dmp.service.feign.IDeviceService;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class DMPV2MobileService implements IDMPV2MobileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2MobileService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private IDeviceService deviceService;

    /*********************************************************************************************
     *
     * DEVICE SERVICES
     *
     */

    @Override
    public ResponseEntity<?> registerPatientDevice(DeviceStat deviceStat) throws InternalException {
        return deviceService.createDeviceStat(deviceStat);
    }

    @Override
    public ResponseEntity<?> updateDeviceStat(DeviceStat deviceStat) throws InternalException {
        return deviceService.updateDeviceStat(deviceStat);
    }

    @Override
    public ResponseEntity<?> configureDevice(DeviceStatConfig deviceStatConfig) throws InternalException {
        if (deviceStatConfig.getId() != null)
            return deviceService.updateDeviceStatConfig(deviceStatConfig);
        else
            return deviceService.createDeviceStatConfig(deviceStatConfig);
    }

    @Override
    public ResponseEntity<?> getDeviceConfiguration(String userId) throws InternalException {
        return deviceService.readDeviceStatConfigByUserId(userId);
    }



}
