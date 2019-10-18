package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPDoctorConstants;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPDoctorRepository extends ResourceUtilRepository<DMPDoctor, String>{

    @Query(value = "{dmpId:'?0', state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    List<DMPDoctor> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    List<DMPDoctor> getAll() throws DataAccessException;

    @Query(value = "{userId:'?0', state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    List<DMPDoctor> getAllByUserId(String userId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    DMPDoctor getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'codeNumber' : {$regex:'?0'} } ], state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    Page<DMPDoctor> query(String searchString, Pageable pageable);

    List<DMPDoctor> findAllByIdInAndState(List<String> ids, int state) throws DataAccessException;

    DMPDoctor findByDmpIdAndCodeNumberAndState(String dmpId, String codeNumber, int state);

    DMPDoctor findByDmpIdAndUserIdAndState(String dmpId, String userId, int state) throws InternalException;

    List<DMPDoctor> findByIdInAndDmpIdAndState(List<String> ids, String dmpId, int state) throws InternalException;

}
