package kz.beeset.med.dmp.repository.dmpv2;


import kz.beeset.med.dmp.constant.DMPV2.DiagnosticsConstants;
import kz.beeset.med.dmp.model.dmpv2.Category;
import kz.beeset.med.dmp.model.dmpv2.Diagnostics;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Repository
public interface DiagnosticsRepository extends ResourceUtilRepository<Diagnostics, String> {

    @Query(value = "{state:"+ DiagnosticsConstants.STATUS_ACTIVE +"}")
    List<Diagnostics> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DiagnosticsConstants.STATUS_ACTIVE +"}")
    Diagnostics getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ DiagnosticsConstants.STATUS_ACTIVE +"}")
    Page<Diagnostics> query(String searchString, Pageable pageable);

    List<Diagnostics> findAllByCategoryIdAndState(String categoryId, int state) throws DataAccessException;

    List<Diagnostics> findAllByIdInAndState(List<String> ids, int state) throws DataAccessException;
}
