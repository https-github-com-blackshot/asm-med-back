package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.dmpv2.DMPV2Visit;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPV2VisitService {
    Page<DMPV2Visit> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPV2Visit> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMPV2Visit> readIterable() throws InternalException;
    List<DMPV2Visit> readIterableByPatientId(String patientId) throws InternalException;
    DMPV2Visit readLastVisit(String patientId) throws InternalException;
    DMPV2Visit readOne(String id) throws InternalException;
    DMPV2Visit create(DMPV2Visit visit) throws InternalException;
    DMPV2Visit update(DMPV2Visit visit) throws InternalException;
    void delete(String id) throws InternalException;
}
