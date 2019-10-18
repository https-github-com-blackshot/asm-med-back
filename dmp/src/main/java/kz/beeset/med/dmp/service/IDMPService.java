package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPService {

    Page<DMP> read(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMP> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMP> read() throws InternalException;
    List<DMP> readByDoctorUserId(String userId) throws InternalException;
    List<DMP> readByPatientUserId(String userId) throws InternalException;
    List<DMP> readByCategory(String category) throws InternalException;
    DMP get(String id) throws InternalException;
    DMP create(DMP dmp) throws InternalException;
    DMP update(DMP dmp) throws InternalException;
    DMP delete(String id) throws InternalException;


}
