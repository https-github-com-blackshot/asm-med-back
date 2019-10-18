package kz.beeset.med.dmp.service.feign;

import kz.beeset.med.dmp.model.feign.DeviceStat;
import kz.beeset.med.dmp.model.feign.DeviceStatConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

import javax.validation.Valid;

@Service
@FeignClient(name = "med-device")
public interface IDeviceService {

    @RequestMapping(value = "/device/create/deviceStat", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> createDeviceStat(@RequestBody @Valid DeviceStat deviceStat);

    @RequestMapping(value = "/device/update/deviceStat", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> updateDeviceStat(@RequestBody @Valid DeviceStat deviceStat);

    @RequestMapping(value = "/device/create/deviceStatConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> createDeviceStatConfig(@RequestBody @Valid DeviceStatConfig deviceStatConfig);

    @RequestMapping(value = "/device/update/deviceStatConfig", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> updateDeviceStatConfig(@RequestBody @Valid DeviceStatConfig deviceStatConfig);

    @RequestMapping(value = "/device/read/deviceStatConfig/byUserId/{userId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> readDeviceStatConfigByUserId(@PathVariable(name = "userId") String userId);

}
