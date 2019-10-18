package kz.beeset.med.dmp.service.dmpv2;


import kz.beeset.med.dmp.model.common.Form;
import kz.beeset.med.dmp.model.dmpv2.Laboratory;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ILaboratoryService {

    Page<Laboratory> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<Laboratory> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<Laboratory> readIterable() throws InternalException;
    List<Laboratory> readIterableByIdIn(List<String> ids) throws InternalException;
    List<Laboratory> readIterableByCategoryid(String categoryId) throws InternalException;
    Object readCategorizedLaboratoriesByIdIn(List<String> ids) throws InternalException;
    Object readCategorizedLaboratories() throws InternalException;
    Laboratory readOne(String id) throws InternalException;
    Laboratory create(Laboratory laboratory) throws InternalException;
    Laboratory update(Laboratory laboratory) throws InternalException;
    void delete(String id) throws InternalException;

}
