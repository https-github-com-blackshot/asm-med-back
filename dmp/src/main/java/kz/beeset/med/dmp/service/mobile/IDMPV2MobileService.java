package kz.beeset.med.dmp.service.mobile;

import kz.beeset.med.dmp.model.feign.DeviceStat;
import kz.beeset.med.dmp.model.feign.DeviceStatConfig;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.http.ResponseEntity;

public interface IDMPV2MobileService {

    /**********************************************************************************
     *  Данные об устройтсве
     */

    ResponseEntity<?> registerPatientDevice(DeviceStat dmpDeviceStat) throws InternalException;
    ResponseEntity<?> updateDeviceStat(DeviceStat dmpDeviceStat) throws InternalException;
    ResponseEntity<?> configureDevice(DeviceStatConfig dmpDeviceStatConfig) throws InternalException;
    ResponseEntity<?> getDeviceConfiguration(String userId) throws InternalException;

}
