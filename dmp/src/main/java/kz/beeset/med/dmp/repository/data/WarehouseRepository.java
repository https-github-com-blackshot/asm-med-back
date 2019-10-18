package kz.beeset.med.dmp.repository.data;

import kz.beeset.med.constructor.model.data.Warehouse;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface WarehouseRepository extends ResourceUtilRepository<Warehouse, String> {

    List<Warehouse> findAllByDmpId(String dmpId) throws DataAccessException;
    List<Warehouse> findAllByDmpIdAndUserId(String dmpId, String userId) throws DataAccessException;
    List<Warehouse> findAllByDmpIdAndVisitId(String dmpId, String visitId) throws DataAccessException;
    List<Warehouse> findAllByDmpIdAndFormId(String dmpId, String formId) throws DataAccessException;
    List<Warehouse> findAllByDmpIdAndUserIdAndVisitId(String dmpId, String userId, String visitId) throws DataAccessException;
    List<Warehouse> findAllByDmpIdAndUserIdAndFormId(String dmpId, String userId, String formId) throws DataAccessException;
    List<Warehouse> findAllByVisitIdAndUserId(String visitId, String userId) throws DataAccessException;
    List<Warehouse> findAllByUserIdAndVisitIdAndFormId(String userId, String visitId, String formId) throws DataAccessException;
    List<Warehouse> findAllByDmpIdAndUserIdAndVisitIdAndFormId(String dmpId, String userId, String visitId, String formId) throws DataAccessException;
    Warehouse findWarehouseByVisitIdAndUserIdAndFormId(String visitId, String userId, String formId) throws InternalException;


}
