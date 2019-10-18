package kz.beeset.med.dmp.repository.dmpv2;



import kz.beeset.med.dmp.constant.DMPV2.DiseaseConstants;
import kz.beeset.med.dmp.model.dmpv2.Disease;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends ResourceUtilRepository<Disease, String> {


    @Query(value = "{state:"+ DiseaseConstants.STATUS_ACTIVE +"}")
    List<Disease> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DiseaseConstants.STATUS_ACTIVE +"}")
    Disease getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ DiseaseConstants.STATUS_ACTIVE +"}")
    Page<Disease> query(String searchString, Pageable pageable);

    List<Disease> findAllByCategoryIdAndState(String categoryId, int state) throws DataAccessException;

    List<Disease> findAllByIdIn(List<String> ids) throws DataAccessException;
}
