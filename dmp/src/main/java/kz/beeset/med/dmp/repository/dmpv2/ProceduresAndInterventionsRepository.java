package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.model.dmpv2.ProceduresAndInterventions;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProceduresAndInterventionsRepository extends ResourceUtilRepository<ProceduresAndInterventions, String> {

    @Query(value = "{state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<ProceduresAndInterventions> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    ProceduresAndInterventions getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    Page<ProceduresAndInterventions> query(String searchString, Pageable pageable);

    List<ProceduresAndInterventions> findAllByCategoryIdAndState(String categoryId, int state) throws DataAccessException;

    List<ProceduresAndInterventions> findAllByIdInAndState(List<String> ids, int state) throws DataAccessException;

}
