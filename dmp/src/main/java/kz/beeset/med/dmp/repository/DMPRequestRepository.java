package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPRequestConstants;
import kz.beeset.med.dmp.model.DMPProtocol;
import kz.beeset.med.dmp.model.DMPRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DMPRequestRepository extends ResourceUtilRepository<DMPRequest, String> {

    @Query(value = "{state:"+ DMPRequestConstants.STATUS_ACTIVE +"}")
    List<DMPRequest> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPRequestConstants.STATUS_ACTIVE +"}")
    DMPRequest getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'fio' : {$regex:'?0'} }, { 'idn' : {$regex:'?0'} }, { 'phone' : {$regex:'?0'} }, { 'email' : {$regex:'?0'} } ], state:"+ DMPRequestConstants.STATUS_ACTIVE +"}")
    Page<DMPRequest> query(String searchString, Pageable pageable);

}
