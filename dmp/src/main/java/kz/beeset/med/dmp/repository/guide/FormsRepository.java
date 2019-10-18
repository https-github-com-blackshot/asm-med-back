package kz.beeset.med.dmp.repository.guide;

import kz.beeset.med.constructor.constant.ConstructorConstants;
import kz.beeset.med.constructor.model.guide.Form;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormsRepository extends MongoRepository<Form, String> {

    @Query("{organizationId: '?0', typeForm: 'grid', state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    List<Form> findAllByOrganizationIdAndFormTypeGrid(String orgId);

    @Query("{organizationId: '?0', typeForm: 'field_group', state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    List<Form> findAllByOrganizationIdAndFormTypeFG(String orgId);

    @Query("{organizationId: '?0', typeForm: 'table', state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    List<Form> findAllByOrganizationIdAndFormTypeTable(String orgId);

    @Query("{state:" + ConstructorConstants.STATUS_ACTIVE + ", typeForm:{$regex:'?0'}}")
    List<Form> getAllByTypeForm(String typeForm);

    @Query("{id:'?0'}")
    Form getById(String id) throws DataAccessException;

    @Query("{organizationId:'?0'}")
    List<Form> getGridsByOrganizationId(String orgazinationId) throws DataAccessException;

    @Query("{id:'?0', state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    Form findFormsById(String id) throws DataAccessException;

    @Query("{state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    Page<Form> readPage(Pageable pageable);

    @Query("{typeForm: '?0',state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    Page<Form> readPageByTypeForm(String typeForm, Pageable pageable);

    @Query("{moduleId: '?0',state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    Page<Form> readPageByModuleId(String kiId, Pageable pageable);

    @Query("{typeForm: '?0', moduleId: '?1', state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    Page<Form> readPageByTypeFormAndModuleId(String typeForm, String kiId, Pageable pageable);

    @Query(value = "{ $or: [ { 'id' : {$regex:'?0'} }, {nameKz:{$regex:'?0'}}, { 'nameRu' : {$regex:'?0'} },  { 'nameEn' : {$regex:'?0'} }, { 'descriptionKz' : {$regex:'?0'} }, { 'descriptionRu' : {$regex:'?0'} }, { 'descriptionEn' : {$regex:'?0'} }, { 'code' : {$regex:'?0'} } ] }")
    Page<Form> query(String searchString, Pageable pageable);

    @Query("{typeForm: '?0', moduleId: '?1', state:" + ConstructorConstants.STATUS_ACTIVE + "}")
    Form getFormByModuleId(String typeForm, String moduleId);

    List<Form> findAllByIdInAndState(List<String> ids, int state);

}
