package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPDoctorConstants;
import kz.beeset.med.dmp.constant.DMPPatientConstants;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPPatientRepository extends ResourceUtilRepository<DMPPatient, String>{

    @Query(value = "{dmpId:'?0', state:"+ DMPPatientConstants.STATUS_ACTIVE +"}")
    List<DMPPatient> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPPatientConstants.STATUS_ACTIVE +"}")
    DMPPatient getById(String id) throws DataAccessException;

    @Query(value = "{userId:'?0', state:"+ DMPDoctorConstants.STATUS_ACTIVE +"}")
    List<DMPPatient> getAllByUserId(String userId) throws DataAccessException;

    @Query(value = "{ $or: [ { 'codeNumber' : {$regex:'?0'} } ], state:"+ DMPPatientConstants.STATUS_ACTIVE +"}")
    Page<DMPPatient> query(String searchString, Pageable pageable);

    List<DMPPatient> findAllByUserIdInAndState(List<String> userIds, int state);

    int countAllByDmpDoctorIdsInAndState(List<String> dmpDoctorIds, int state);

    List<DMPPatient> findAllByDmpDoctorIdsInAndState(List<String> doctorIds, int state);

    List<DMPPatient> findAllByDmpIdAndDmpDoctorIdsInAndState(String dmpId, String dmpDoctorId, int state);

    DMPPatient findByUserIdAndDmpIdAndState(String userId, String dmpId, int state);

}
