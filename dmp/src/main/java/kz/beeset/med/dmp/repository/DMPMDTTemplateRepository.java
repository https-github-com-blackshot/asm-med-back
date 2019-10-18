package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.constant.DMPMDTEventConstants;
import kz.beeset.med.dmp.constant.DMPMDTTemplateConstants;
import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.DMPMDTTemplate;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPMDTTemplateRepository extends ResourceUtilRepository<DMPMDTTemplate, String>{

        @Query(value = "{dmpId:'?0', state:"+ DMPMDTTemplateConstants.STATUS_ACTIVE +"}")
        List<DMPMDTTemplate> getAllByDMPId(String dmpId) throws DataAccessException;

        @Query(value = "{id:'?0', state:"+ DMPMDTTemplateConstants.STATUS_ACTIVE +"}")
        DMPMDTTemplate getById(String id) throws DataAccessException;

        @Query(value = "{$or: [ { 'ownerId': '?0' }], state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
        List<DMPMDTTemplate> getByUserId(String id) throws DataAccessException;

        @Query(value = "{ $or: [ { 'name' : {$regex:'?0'} } ], state:"+ DMPMDTTemplateConstants.STATUS_ACTIVE +"}")
        Page<DMPMDTTemplate> query(String searchString, Pageable pageable);

        List<DMPMDTTemplate> getAllByDoctorsContains(String userId) throws InternalException;

        int countAllByOwnerIdAndState(String userId, int state) throws InternalException;

}
