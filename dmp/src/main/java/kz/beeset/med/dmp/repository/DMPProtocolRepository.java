package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPProtocolConstants;
import kz.beeset.med.dmp.model.DMPProtocol;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPProtocolRepository extends ResourceUtilRepository<DMPProtocol, String> {

    @Query(value = "{dmpId:'?0', state:"+ DMPProtocolConstants.STATUS_ACTIVE +"}")
    List<DMPProtocol> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPProtocolConstants.STATUS_ACTIVE +"}")
    DMPProtocol getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ DMPProtocolConstants.STATUS_ACTIVE +"}")
    Page<DMPProtocol> query(String searchString, Pageable pageable);

    List<DMPProtocol> findAllByDmpIdInAndState(List<String> dmpIds, int state);

    int countAllByDmpIdInAndState(List<String> dmpIds, int state);

}
