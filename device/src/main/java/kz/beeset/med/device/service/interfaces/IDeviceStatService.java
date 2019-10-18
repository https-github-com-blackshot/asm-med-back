package kz.beeset.med.device.service.interfaces;

import kz.beeset.med.device.model.DeviceStat;
import kz.beeset.med.device.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDeviceStatService {

    Page<DeviceStat> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<DeviceStat> search(Map<String, String> allRequestParams) throws InternalException;
    List<DeviceStat> readIterableByUserId(String userId) throws InternalException;
    DeviceStat getLastRecordByUserId(String userId) throws InternalException;
    DeviceStat get(String id) throws InternalException;
    DeviceStat create(DeviceStat deviceStat) throws InternalException;
    DeviceStat update(DeviceStat deviceStat) throws InternalException;
    DeviceStat delete(String id) throws InternalException;
    int defineHealthStatus(DeviceStat deviceStat) throws InternalException;

}
