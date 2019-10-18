package kz.beeset.med.device.repository;

import kz.beeset.med.device.constant.DefaultConstant;
import kz.beeset.med.device.model.DeviceStatConfig;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceStatConfigRepository extends ResourceUtilRepository<DeviceStatConfig, String> {

    @Query(value = "{userId:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    DeviceStatConfig getByUserId(String userId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    DeviceStatConfig getById(String id) throws DataAccessException;

    List<DeviceStatConfig> findAllByUserIdInAndState(List<String> userIds, int state);

}
