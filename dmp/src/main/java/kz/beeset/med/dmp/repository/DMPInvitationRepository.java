package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPInvitationConstants;
import kz.beeset.med.dmp.model.DMPInvitation;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPInvitationRepository extends ResourceUtilRepository<DMPInvitation, String>{

    @Query(value = "{dmpId:'?0', state:"+ DMPInvitationConstants.STATUS_ACTIVE +"}")
    List<DMPInvitation> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPInvitationConstants.STATUS_ACTIVE +"}")
    DMPInvitation getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'message' : {$regex:'?0'} }, { 'invitationStatus' : {$regex:'?0'} } ], state:"+ DMPInvitationConstants.STATUS_ACTIVE +"}")
    Page<DMPInvitation> query(String searchString, Pageable pageable);

}
