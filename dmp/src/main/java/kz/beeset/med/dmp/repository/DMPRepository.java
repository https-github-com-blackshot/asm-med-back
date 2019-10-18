package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.model.DMP;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPRepository extends ResourceUtilRepository<DMP, String>{

    @Query(value = "{state:"+ DMPConstants.STATUS_ACTIVE +"}")
    List<DMP> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPConstants.STATUS_ACTIVE +"}")
    DMP getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'nameRu' : {$regex:'?0'} },  { 'nameEn' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ DMPConstants.STATUS_ACTIVE +"}")
    Page<DMP> query(String searchString, Pageable pageable);

    List<DMP> findAllByIdInAndState(List<String> ids, int state) throws DataAccessException;

    List<DMP> findAllByCategoryAndState(String category, int state) throws DataAccessException;


}
