package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.constant.DefaultConstant;
import kz.beeset.med.admin.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends ResourceUtilRepository<Organization, String> {

    /**
     * Поиск по ID
     * @param id - идентификатор
     * @return - возвращает одного пользователя
     */
    @Query("{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    public Organization getById(String id);

    @Query("{path: {$regex:'^?0'}, state:"+ DefaultConstant.STATUS_ACTIVE +"}") //"{'path': {$regex:'^?0'}, 'isActive': {$gte: 1} }")
    public List<Organization> readPath(String path, Pageable page);

    @Query("{state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<Organization> findAll();

    @Query(value = "{code:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}", count = true)
    public Long countDocuments();

    @Query(value = "{code:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<Organization> findByCode(String code);

    //{ $or: [ { 'username' : {$regex:'?0'} }, {idn:{$regex:'?0'}}, { 'name' : {$regex:'?0'} },  { 'surname' : {$regex:'?0'} }, { 'middlename' : {$regex:'?0'} } ] }
    @Query(value = "{ $or: [ { 'nameRu' : {$regex:'?0'} }, {nameKz:{$regex:'?0'}}, { 'nameEn' : {$regex:'?0'} },  { 'code' : {$regex:'?0'} } ] }")//"{code:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    Page<Organization> query(String searchString, Pageable pageableRequest);
}
