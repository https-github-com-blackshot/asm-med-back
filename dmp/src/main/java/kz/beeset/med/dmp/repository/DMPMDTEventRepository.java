package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPMDTEventConstants;
import kz.beeset.med.dmp.model.DMPMDTEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.print.DocFlavor;
import java.util.List;

@Repository
public interface DMPMDTEventRepository extends ResourceUtilRepository<DMPMDTEvent, String>{

    @Query(value = "{dmpId:'?0', state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    List<DMPMDTEvent> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    DMPMDTEvent getById(String id) throws DataAccessException;

    @Query(value = "{$or: [ { 'pendingUserIdList' : '?0' }, { 'ownerId': '?0' }, { 'acceptedDoctorUserIdList' : '?0' } ], state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    List<DMPMDTEvent> getByUserId(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'description' : {$regex:'?0'} } ], $or: [ { 'pendingUserIdList' : '?1' }, { 'ownerId': '?1' }, { 'acceptedDoctorUserIdList' : '?1' } ], state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    Page<DMPMDTEvent> query(String searchString, String userId, Pageable pageable);

    int countAllByPendingDoctorUserIdListContainsOrAcceptedDoctorUserIdListContainsOrOwnerIdAndState(String userId1, String usetId2, String userId3, int state);

}
