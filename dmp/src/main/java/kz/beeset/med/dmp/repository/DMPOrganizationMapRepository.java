package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPOrganizationMap;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPOrganizationMapRepository extends ResourceUtilRepository<DMPOrganizationMap, String> {

    List<DMPOrganizationMap> findAllByOrganizationIdAndState(String organizationId, int state) throws DataAccessException;
    List<DMPOrganizationMap> findAllByOrganizationIdAndDmpIdInAndState(String organizationId, List<String> dmpIds, int state);
    DMPOrganizationMap findByOrganizationIdAndDmpIdAndState(String organizationId, String dmpId, int state) throws DataAccessException;

}
