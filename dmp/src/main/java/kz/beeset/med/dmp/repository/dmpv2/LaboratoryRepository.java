package kz.beeset.med.dmp.repository.dmpv2;


import kz.beeset.med.dmp.constant.DMPV2.LaboratoryConstants;
import kz.beeset.med.dmp.model.dmpv2.Laboratory;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratoryRepository extends ResourceUtilRepository<Laboratory, String> {


    @Query(value = "{state:"+ LaboratoryConstants.STATUS_ACTIVE +"}")
    List<Laboratory> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ LaboratoryConstants.STATUS_ACTIVE +"}")
    Laboratory getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ LaboratoryConstants.STATUS_ACTIVE +"}")
    Page<Laboratory> query(String searchString, Pageable pageable);

    List<Laboratory> findAllByCategoryIdAndState(String categoryId, int state) throws DataAccessException;

    List<Laboratory> findAllByIdInAndState(List<String> ids, int state) throws DataAccessException;

}
