package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.common.Form;
import kz.beeset.med.dmp.model.dmpv2.Diagnostics;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDiagnosticsService {

    Page<Diagnostics> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<Diagnostics> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<Diagnostics> readIterable() throws InternalException;
    List<Diagnostics> readIterableByIdIn(List<String> ids) throws InternalException;
    List<Diagnostics> readIterableByCategoryId(String categoryId) throws InternalException;
    Object readCategorizedDiagnosticsByIdIn(List<String> ids) throws InternalException;
    Object readCategorizedDiagnostics() throws InternalException;
    Diagnostics readOne(String id) throws InternalException;
    Diagnostics create(Diagnostics diagnostics) throws InternalException;
    Diagnostics update(Diagnostics diagnostics) throws InternalException;
    void delete(String id) throws InternalException;

    List<Form> readFormsByDiagnosticsId(String diagnosticsId) throws InternalException;

}
