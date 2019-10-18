package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.dmpv2.DMPV2;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPV2Service {
    Page<DMPV2> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPV2> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMPV2> readIterable() throws InternalException;
    DMPV2 readOne(String id) throws InternalException;
    DMPV2 updateAfterDiseaseSelection(DMPV2 dmp) throws InternalException;
    DMPV2 create(DMPV2 category) throws InternalException;
    DMPV2 update(DMPV2 category) throws InternalException;
    void delete(String id) throws InternalException;
}
