package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPDeviceStatConfigConstants;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPDeviceStatConfigRepository extends ResourceUtilRepository<DMPDeviceStatConfig, String> {

    @Query(value = "{dmpId:'?0', state:"+ DMPDeviceStatConfigConstants.STATUS_ACTIVE +"}")
    List<DMPDeviceStatConfig> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPDeviceStatConfigConstants.STATUS_ACTIVE +"}")
    DMPDeviceStatConfig getById(String id) throws DataAccessException;

    @Query(value = "{dmpPatientId:'?0', state:"+ DMPDeviceStatConfigConstants.STATUS_ACTIVE +"}")
    DMPDeviceStatConfig getByDMPPatientId(String dmpPatientId) throws DataAccessException;

    List<DMPDeviceStatConfig> findAllByDmpPatientIdInAndState(List<String> dmpPatientIdList, int state);

}
