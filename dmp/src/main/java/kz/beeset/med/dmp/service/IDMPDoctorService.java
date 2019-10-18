package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.custom.DMPDoctorCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPDoctorService {

    Page<DMPDoctor> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPDoctorCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPDoctorCustom> searchCustom(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPDoctor> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPDoctor> readIterableByDMPId(String dmpId) throws InternalException;
    List<DMPDoctor> readIterableByIds(List<String> dmpDoctorIds) throws InternalException;
    List<DMPDoctorCustom> readIterableByTemplateId(String dmpId, String templateId) throws InternalException;
    DMPDoctor get(String id) throws InternalException;
    List<DMPDoctor> getAllByUserId(String id) throws InternalException;
    DMPDoctor create(DMPDoctor dmpDoctor) throws InternalException;
    DMPDoctor update(DMPDoctor dmpDoctor) throws InternalException;
    DMPDoctor delete(String id) throws InternalException;

    List<String> getDoctorUserIdByPatientUserIdAndDMPId(String userId, String dmpId) throws InternalException;

}
