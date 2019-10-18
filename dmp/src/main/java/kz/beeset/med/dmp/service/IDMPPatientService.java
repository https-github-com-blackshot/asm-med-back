package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPPatientService {

    Page<DMPPatientCustom> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPPatientCustom> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPPatient> readIterableByDMPId(String dmpId) throws InternalException;
    List<DMPPatientCustom> readIterableByDMPIdAndDMPDoctorUserId(String dmpId, String dmpDoctorUserId) throws InternalException;
    DMPPatient get(String id) throws InternalException;
    List<DMPPatient> getAllByUserId(String id) throws InternalException;
    List<DMPPatient> getAllByDoctorUserId(String userId) throws InternalException;
    DMPPatient create(DMPPatient dmpPatient) throws InternalException;
    DMPPatient update(DMPPatient dmpPatient) throws InternalException;
    DMPPatient delete(String id) throws InternalException;
    DMPPatient createPatientTemp(String patientIdn, String doctorCodeNumber, DMPPatient dmpPatient) throws InternalException;

}
