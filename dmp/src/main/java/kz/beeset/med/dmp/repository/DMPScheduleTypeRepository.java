package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPScheduleTypeConstants;
import kz.beeset.med.dmp.model.DMPScheduleType;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPScheduleTypeRepository extends ResourceUtilRepository<DMPScheduleType, String>{

    @Query(value = "{dmpId:'?0', state:"+ DMPScheduleTypeConstants.STATUS_ACTIVE +"}")
    List<DMPScheduleType> getAllByDMPId(String dmpId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPScheduleTypeConstants.STATUS_ACTIVE +"}")
    DMPScheduleType getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'nameKz' : {$regex:'?0'} }, { 'nameRu' : {$regex:'?0'} }, { 'nameEn' : {$regex:'?0'} }, { 'type' : {$regex:'?0'} }, { 'visitCount' : {$regex:'?0'} } ], dmpId: '?1' ,state:"+ DMPScheduleTypeConstants.STATUS_ACTIVE +"}")
    Page<DMPScheduleType> query(String searchString, String dmpId, Pageable pageable);

}
