package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPDeviceStatConfigService {

    Page<DMPDeviceStatConfig> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    List<DMPDeviceStatConfig> readIterableByDMPId(String dmpId) throws InternalException;
    DMPDeviceStatConfig get(String id) throws InternalException;
    DMPDeviceStatConfig create(DMPDeviceStatConfig dmpDeviceStatConfig) throws InternalException;
    DMPDeviceStatConfig update(DMPDeviceStatConfig dmpDeviceStatConfig) throws InternalException;
    DMPDeviceStatConfig delete(String id) throws InternalException;


}
