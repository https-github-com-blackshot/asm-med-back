package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.dmp.constant.DMPMDTEventConstants;
import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTEvent;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPV2MDTEventRepository extends ResourceUtilRepository<DMPV2MDTEvent, String> {

    @Query(value = "{diseaseId:'?0', state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    List<DMPV2MDTEvent> getAllByDiseaseId(String diseaseId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    DMPV2MDTEvent getById(String id) throws DataAccessException;

    @Query(value = "{$or: [ { 'pendingUserIdList' : '?0' }, { 'ownerId': '?0' }, { 'acceptedDoctorUserIdList' : '?0' } ], state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    List<DMPV2MDTEvent> getByUserId(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'description' : {$regex:'?0'} } ], $or: [ { 'pendingUserIdList' : '?1' }, { 'ownerId': '?1' }, { 'acceptedDoctorUserIdList' : '?1' } ], state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    Page<DMPV2MDTEvent> query(String searchString, String userId, Pageable pageable);

    int countAllByPendingDoctorUserIdListContainsOrAcceptedDoctorUserIdListContainsOrOwnerIdAndState(String userId1, String usetId2, String userId3, int state);

}
