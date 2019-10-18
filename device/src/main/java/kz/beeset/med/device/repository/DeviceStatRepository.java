package kz.beeset.med.device.repository;

import kz.beeset.med.device.constant.DefaultConstant;
import kz.beeset.med.device.model.DeviceStat;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceStatRepository extends ResourceUtilRepository<DeviceStat, String> {

    @Query(value = "{userId:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<DeviceStat> getAllByUserId(String userId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    DeviceStat getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'checkDate' : {$regex:'?0'} }, { 'deviceName' : {$regex:'?0'} } ], state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    Page<DeviceStat> query(String searchString, Pageable pageable);

    @Query("{userId:'?0', state:" + DefaultConstant.STATUS_ACTIVE + "}")
    List<DeviceStat> findAllByUserIdSortedByCreatedDate(String userId, Sort sort);

    List<DeviceStat> findAllByUserIdAndStateAndCheckDateBetweenOrderByCheckDateAsc(String userId, int state, LocalDateTime d1, LocalDateTime d2);

}
