package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPOrganizationMap;
import kz.beeset.med.dmp.utils.error.InternalException;

import java.util.HashMap;
import java.util.List;

public interface IDMPOrganizationMapService {

    List<DMP> getDMPListByOrganizationId(String organizationId) throws InternalException;
    List<DMPOrganizationMap> save(String organizationId, HashMap<String, Boolean> checkboxes) throws InternalException;
    void delete(String organizationId, String dmpId) throws InternalException;

}
