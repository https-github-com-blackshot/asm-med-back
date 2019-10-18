package kz.beeset.med.device.service.interfaces;

import kz.beeset.med.device.model.DeviceStatConfig;
import kz.beeset.med.device.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDeviceStatConfigService {

    Page<DeviceStatConfig> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    List<DeviceStatConfig> readIterable() throws InternalException;
    DeviceStatConfig readByUserId(String userId) throws InternalException;
    DeviceStatConfig read(String id) throws InternalException;
    DeviceStatConfig create(DeviceStatConfig deviceStatConfig) throws InternalException;
    DeviceStatConfig update(DeviceStatConfig deviceStatConfig) throws InternalException;
    DeviceStatConfig delete(String id) throws InternalException;

}
