package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPDoctorConstants;
import kz.beeset.med.dmp.constant.DMPNotificationConstants;
import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.DMPPatient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPNotificationRepository extends ResourceUtilRepository<DMPNotification, String> {

    @Query(value = "{dmpId:'?0', state:"+ DMPNotificationConstants.STATUS_ACTIVE +"}")
    List<DMPNotification> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPNotificationConstants.STATUS_ACTIVE +"}")
    DMPNotification getById(String id) throws DataAccessException;

    @Query(value = "{dmpDoctorUserId:'?0', state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    List<DMPNotification> getAllByUserId(String userId) throws DataAccessException;

    @Query(value = "{ $or: [ { 'type' : {$regex:'?0'} }, { 'message' : {$regex:'?0'} }, { 'beginDate' : {$regex:'?0'} }, { 'endDate' : {$regex:'?0'} }, { 'period' : {$regex:'?0'} }, { 'eventTime' : {$regex:'?0'} } ], state:"+ DMPNotificationConstants.STATUS_ACTIVE +"}")
    Page<DMPNotification> query(String searchString, Pageable pageable);

    int countAllByDmpDoctorUserIdAndState(String dmpDoctorUserId, int state);

}
