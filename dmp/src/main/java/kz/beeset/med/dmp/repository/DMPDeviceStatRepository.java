package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPDeviceStatConstants;
import kz.beeset.med.dmp.model.DMPDeviceStat;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DMPDeviceStatRepository extends ResourceUtilRepository<DMPDeviceStat, String> {

    @Query(value = "{dmpId:'?0', state:"+ DMPDeviceStatConstants.STATUS_ACTIVE +"}")
    List<DMPDeviceStat> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPDeviceStatConstants.STATUS_ACTIVE +"}")
    DMPDeviceStat getById(String id) throws DataAccessException;

    @Query(value = "{dmpPatientId:'?0', state:"+ DMPDeviceStatConstants.STATUS_ACTIVE +"}")
    DMPDeviceStat getByDMPPatientId(String dmpPatientId) throws DataAccessException;

    @Query(value = "{ $or: [ { 'checkDate' : {$regex:'?0'} }, { 'deviceInfo' : {$regex:'?0'} } ], state:"+ DMPDeviceStatConstants.STATUS_ACTIVE +"}")
    Page<DMPDeviceStat> query(String searchString, Pageable pageable);

    List<DMPDeviceStat> findAllByDmpPatientIdInAndState(List<String> dmpPatientIdList, int state);

    @Query("{dmpPatientId:'?0', state:" + DMPDeviceStatConstants.STATUS_ACTIVE + "}")
    List<DMPDeviceStat> findAllByDMPPatientIdSortedByCreatedDate(String dmpPatientId, Sort sort);
    List<DMPDeviceStat> findAllByDmpPatientIdAndStateAndCreatedDateBetween(String dmpPatientId, int state, LocalDateTime d1, LocalDateTime d2);
    List<DMPDeviceStat> findAllByDmpPatientIdAndStateAndCreatedDateWithin(String dmpPatientId, int state, LocalDateTime d1, LocalDateTime d2);
    List<DMPDeviceStat> findAllByDmpPatientIdAndStateOrderByCreatedDateAsc(String dmpPatientId, int state);


    List<DMPDeviceStat> findAllByDmpPatientIdAndStateAndCheckDateBetweenOrderByCheckDateAsc(String dmpPatientId, int state, LocalDateTime d1, LocalDateTime d2);

}
