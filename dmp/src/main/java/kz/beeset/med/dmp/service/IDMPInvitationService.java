package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPInvitation;
import kz.beeset.med.dmp.model.custom.DMPInvitationCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPInvitationService {

    Page<DMPInvitation> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPInvitation> search(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPInvitationCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPInvitationCustom> searchCustomPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMPInvitation> readIterableByDMPId(String dmpId) throws InternalException;
    DMPInvitation get(String id) throws InternalException;
    DMPInvitation create(DMPInvitation dmpInvitation) throws InternalException;
    DMPInvitation update(DMPInvitation dmpInvitation) throws InternalException;
    DMPInvitation delete(String id) throws InternalException;

}
