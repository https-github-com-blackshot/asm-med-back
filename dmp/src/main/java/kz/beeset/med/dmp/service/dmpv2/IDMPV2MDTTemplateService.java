package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTTemplate;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPV2MDTTemplateService {

    Page<DMPV2MDTTemplate> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPV2MDTTemplate> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPV2MDTTemplate> readIterableByDiseaseId(String dmpId) throws InternalException;
    DMPV2MDTTemplate get(String id) throws InternalException;
    List<DMPV2MDTTemplate> getByUserId(String id) throws InternalException;
    DMPV2MDTTemplate create(DMPV2MDTTemplate dmpmdtTemplate) throws InternalException;
    DMPV2MDTTemplate update(DMPV2MDTTemplate dmpmdtTemplate) throws InternalException;
    DMPV2MDTTemplate delete(String id) throws InternalException;

}
