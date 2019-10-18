package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.dmp.constant.DMPV2.MedicineConstants;
import kz.beeset.med.dmp.model.dmpv2.Medicine;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends ResourceUtilRepository<Medicine, String> {


    @Query(value = "{state:"+ MedicineConstants.STATUS_ACTIVE +"}")
    List<Medicine> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ MedicineConstants.STATUS_ACTIVE +"}")
    Medicine getById(String id) throws DataAccessException;

    @Query(value = "{ $or: [ { 'code' : {$regex:'?0'} }, { 'name' : {$regex:'?0'} }, { 'description' : {$regex:'?0'} } ], state:"+ MedicineConstants.STATUS_ACTIVE +"}")
    Page<Medicine> query(String searchString, Pageable pageable);

    List<Medicine> findAllByCategoryIdAndState(String categoryId, int state) throws DataAccessException;

    List<Medicine> findAllByIdInAndState(List<String> ids, int state) throws DataAccessException;
}
