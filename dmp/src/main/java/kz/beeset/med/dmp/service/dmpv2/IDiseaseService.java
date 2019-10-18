package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.common.Form;
import kz.beeset.med.dmp.model.dmpv2.Diagnostics;
import kz.beeset.med.dmp.model.dmpv2.Disease;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDiseaseService {

    Page<Disease> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<Disease> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<Disease> readIterable() throws InternalException;
    List<Disease> readIterableByCategoryId(String categoryId) throws InternalException;
    List<Disease> readIterableByIdIn(List<String> diseaseIds) throws InternalException;
    Disease readOne(String id) throws InternalException;
    Disease create(Disease disease) throws InternalException;
    Disease update(Disease disease) throws InternalException;
    void delete(String id) throws InternalException;

    List<Form> readFormsByDiseaseId(String diseaseId) throws InternalException;
}
