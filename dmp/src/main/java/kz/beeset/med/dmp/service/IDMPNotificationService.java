package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.custom.DMPNotificationCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPNotificationService {

    Page<DMPNotification> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPNotification> search(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPNotificationCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPNotificationCustom> searchCustomPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMPNotification> readIterableByDMPId(String dmpId) throws InternalException;
    DMPNotification get(String id) throws InternalException;
    List<DMPNotification> getAllByUserId(String id) throws InternalException;
    DMPNotification create(DMPNotification dmpNotification) throws InternalException;
    DMPNotification update(DMPNotification dmpNotification) throws InternalException;
    DMPNotification delete(String id) throws InternalException;

}
