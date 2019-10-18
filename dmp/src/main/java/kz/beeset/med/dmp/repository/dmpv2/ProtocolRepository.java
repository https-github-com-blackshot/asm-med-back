package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.model.dmpv2.Protocol;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProtocolRepository extends ResourceUtilRepository<Protocol, String> {


    @Query(value = "{state:"+ DMPConstants.STATUS_ACTIVE +"}")
    List<Protocol> getAll() throws DataAccessException;

    @Query(value = "{diseaseId:'?0', state:"+ DMPConstants.STATUS_ACTIVE +"}")
    List<Protocol> getAllByDiseaseId(String diseaseId) throws DataAccessException;

    List<Protocol> findAllByDiseaseIdInAndStateIs(List<String> ids, int state) throws DataAccessException;

    @Query(value = "{ $or: [ { 'name' : {$regex:'?0'}},{ 'code' : {$regex:'?0'}},{ 'description' : {$regex:'?0'} }   ], state:"+ DMPConstants.STATUS_ACTIVE +"}")
    Page<Protocol> query(String searchString, Pageable pageable);

    @Query(value = "{id:'?0', state:"+ DMPConstants.STATUS_ACTIVE +"}")
    Protocol getById(String id) throws DataAccessException;


}
