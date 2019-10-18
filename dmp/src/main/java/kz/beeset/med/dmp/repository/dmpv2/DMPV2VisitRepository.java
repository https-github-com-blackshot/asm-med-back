package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.constant.DMPDeviceStatConstants;
import kz.beeset.med.dmp.constant.DMPV2.DiseaseConstants;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Visit;
import kz.beeset.med.dmp.model.dmpv2.Disease;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPV2VisitRepository extends ResourceUtilRepository<DMPV2Visit, String> {

    @Query(value = "{state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    List<DMPV2Visit> getAll() throws DataAccessException;

    @Query(value = "{id:'?0', state:"+ DefaultConstant.STATUS_ACTIVE +"}")
    DMPV2Visit getById(String id) throws DataAccessException;

    List<DMPV2Visit> findAllByPatientIdAndStateOrderByCounterDesc(String patientId, int state);
}
