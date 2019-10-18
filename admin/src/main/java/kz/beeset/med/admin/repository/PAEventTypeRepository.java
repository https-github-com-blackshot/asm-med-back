package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.catalog.PAEventType;
import org.springframework.dao.DataAccessException;

public interface PAEventTypeRepository extends ResourceUtilRepository<PAEventType,String> {
    PAEventType getById(String id) throws DataAccessException;
    void deleteEventTypeById(String id) throws DataAccessException;
}
