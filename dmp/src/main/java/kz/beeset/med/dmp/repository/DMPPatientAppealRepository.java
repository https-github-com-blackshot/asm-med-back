package kz.beeset.med.dmp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import kz.beeset.med.dmp.model.DMPPatientAppeal;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPPatientAppealRepository extends ResourceUtilRepository<DMPPatientAppeal, String>{

    List<DMPPatientAppeal> findAllByDmpDoctorIdAndStatus(String dmpDoctorId, String status);
    List<DMPPatientAppeal> findAllByStatus(String status);
    List<DMPPatientAppeal> findAllByDmpDoctorIdAndStatusAndDmpId(String dmpDoctorId, String status, String dmpId);
    List<DMPPatientAppeal> findAllByIdIn(List<String> ids);
    int countAllByDmpDoctorIdInAndStatus(List<String> dmpDoctorIds, String status);

    Page<DMPPatientAppeal> getAllByDmpDoctorIdAndDmpIdAndStatus(String dmpDoctorId, String dmpId, String status, Pageable pageable) throws DataAccessException;

}
