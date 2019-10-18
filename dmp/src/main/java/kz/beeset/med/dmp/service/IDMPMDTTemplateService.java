package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.DMPMDTTemplate;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPMDTTemplateService {

    Page<DMPMDTTemplate> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPMDTTemplate> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPMDTTemplate> readIterableByDMPId(String dmpId) throws InternalException;
    DMPMDTTemplate get(String id) throws InternalException;
    List<DMPMDTTemplate> getByUserId(String id) throws InternalException;
    DMPMDTTemplate create(DMPMDTTemplate dmpmdtTemplate) throws InternalException;
    DMPMDTTemplate update(DMPMDTTemplate dmpmdtTemplate) throws InternalException;
    DMPMDTTemplate delete(String id) throws InternalException;

}
