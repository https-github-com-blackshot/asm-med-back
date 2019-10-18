package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.catalog.PAStatus;
import org.springframework.dao.DataAccessException;

public interface PAStatusRepository extends ResourceUtilRepository<PAStatus,String> {
    PAStatus getById(String id) throws DataAccessException;
    void deleteStatusById(String id) throws DataAccessException;
}
