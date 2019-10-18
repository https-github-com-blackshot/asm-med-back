package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.model.dmpv2.DMPV2;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPV2Repository extends ResourceUtilRepository<DMPV2, String> {

    @Query(value = "{state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<DMPV2> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    DMPV2 getById(String id) throws DataAccessException;
}
