package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPVisitConstants;
import kz.beeset.med.dmp.model.DMPVisit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPVisitRepository extends ResourceUtilRepository<DMPVisit, String>{

    @Query(value = "{dmpId:'?0', state:"+ DMPVisitConstants.STATUS_ACTIVE +"}")
    List<DMPVisit> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{dmpScheduleTypeId:'?0', state:"+ DMPVisitConstants.STATUS_ACTIVE +"}")
    List<DMPVisit> getAllByDMPScheduleTypeId(String dmpScheduleTypeId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPVisitConstants.STATUS_ACTIVE +"}")
    DMPVisit getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'nameKz' : {$regex:'?0'} }, { 'nameRu' : {$regex:'?0'} }, { 'nameEn' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} }, { 'days' : {$regex:'?0'} } ], state:"+ DMPVisitConstants.STATUS_ACTIVE +"}")
    Page<DMPVisit> query(String searchString, Pageable pageable);

    @Query("{dmpId:'?0', code:'?1', state:"+ DMPConstants.STATUS_ACTIVE +"}")
    DMPVisit getByDmpIdAndCode(String dmpId, String code) throws DataAccessException;

    List<DMPVisit> findAllByDmpIdAndDmpScheduleTypeIdAndState(String dmpId, String dmpScheduleTypeId, int state);
}
