package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.dmp.constant.DMPMDTEventConstants;
import kz.beeset.med.dmp.constant.DMPMDTTemplateConstants;
import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTTemplate;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DMPV2MDTTemplateRepository extends ResourceUtilRepository<DMPV2MDTTemplate, String> {

    @Query(value = "{diseaseId:'?0', state:"+ DMPMDTTemplateConstants.STATUS_ACTIVE +"}")
    List<DMPV2MDTTemplate> getAllByDiseaseId(String diseaseId) throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DMPMDTTemplateConstants.STATUS_ACTIVE +"}")
    DMPV2MDTTemplate getById(String id) throws DataAccessException;

    @Query(value = "{$or: [ { 'ownerId': '?0' }], state:"+ DMPMDTEventConstants.STATUS_ACTIVE +"}")
    List<DMPV2MDTTemplate> getByUserId(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'name' : {$regex:'?0'} } ], state:"+ DMPMDTTemplateConstants.STATUS_ACTIVE +"}")
    Page<DMPV2MDTTemplate> query(String searchString, Pageable pageable);

    List<DMPV2MDTTemplate> getAllByDoctorsContains(String userId) throws InternalException;

    int countAllByOwnerIdAndState(String userId, int state) throws InternalException;

}
