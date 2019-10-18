package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.dmpv2.Laboratory;
import kz.beeset.med.dmp.model.dmpv2.ProceduresAndInterventions;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IProceduresAndInterventionsService {

    Page<ProceduresAndInterventions> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<ProceduresAndInterventions> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<ProceduresAndInterventions> readIterable() throws InternalException;
    List<ProceduresAndInterventions> readIterableByIdIn(List<String> ids) throws InternalException;
    List<ProceduresAndInterventions> readIterableByCategoryid(String categoryId) throws InternalException;
    Object readCategorizedListByIdIn(List<String> ids) throws InternalException;
    Object readCategorizedList() throws InternalException;
    ProceduresAndInterventions readOne(String id) throws InternalException;
    ProceduresAndInterventions create(ProceduresAndInterventions proceduresAndInterventions) throws InternalException;
    ProceduresAndInterventions update(ProceduresAndInterventions proceduresAndInterventions) throws InternalException;
    void delete(String id) throws InternalException;

}
