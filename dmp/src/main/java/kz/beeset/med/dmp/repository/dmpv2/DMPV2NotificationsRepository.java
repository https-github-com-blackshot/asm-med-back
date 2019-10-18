package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Notifications;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPV2NotificationsRepository extends ResourceUtilRepository<DMPV2Notifications, String> {

    @Query(value = "{profileId:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<DMPV2Notifications> getAllByProfileId(String profileId) throws DataAccessException;

    @Query(value = "{doctorUserId:'?0', patientUserId:'?1', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<DMPV2Notifications> getAllByDoctorUserIdAndPatientUserId(String doctorUserId, String patientUserId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    DMPV2Notifications getById(String id) throws DataAccessException;

    @Query(value = "{patientUserId:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<DMPV2Notifications> getAllByPatientUserId(String patientUserId) throws DataAccessException;

    @Query(value = "{ $or: [ { 'type' : {$regex:'?0'} }, { 'message' : {$regex:'?0'} }, { 'beginDate' : {$regex:'?0'} }, { 'endDate' : {$regex:'?0'} }, { 'period' : {$regex:'?0'} }, { 'eventTime' : {$regex:'?0'} } ], state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    Page<DMPV2Notifications> query(String searchString, Pageable pageable);

}
