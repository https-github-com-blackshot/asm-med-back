package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPV2.CategoryConstants;

import kz.beeset.med.dmp.model.dmpv2.Category;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends ResourceUtilRepository<Category, String> {

    @Query(value = "{state:"+ CategoryConstants.STATUS_ACTIVE +"}")
    List<Category> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ CategoryConstants.STATUS_ACTIVE +"}")
    Category getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ CategoryConstants.STATUS_ACTIVE +"}")
    Page<Category> query(String searchString, Pageable pageable);

    List<Category> findAllByFilterAndState(String filter, int state) throws DataAccessException;

}
