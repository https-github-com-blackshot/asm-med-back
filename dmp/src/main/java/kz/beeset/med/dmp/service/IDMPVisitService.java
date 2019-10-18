package kz.beeset.med.dmp.service;

import kz.beeset.med.constructor.model.data.Warehouse;
import kz.beeset.med.dmp.model.DMPVisit;
import kz.beeset.med.constructor.model.guide.Form;
import kz.beeset.med.dmp.model.custom.CustomForm;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPVisitService {

    Page<DMPVisit> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPVisit> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPVisit> readIterableByDMPId(String dmpId) throws InternalException;
    List<DMPVisit> readIterableByDMPScheduleTypeId(String dmpScheduleTypeId) throws InternalException;
    List<Form> readFormsByVisitId(String visitId) throws InternalException;
    DMPVisit get(String id) throws InternalException;
    DMPVisit create(DMPVisit dmpVisit) throws InternalException;
    DMPVisit update(DMPVisit dmpVisit) throws InternalException;
    DMPVisit delete(String id) throws InternalException;
    String duplicateDmpVisit(String visitId, String code) throws InternalException;
    DMPVisit getDmpVisitById(String id) throws InternalException;
    DMPVisit getByCodeAndDMPId(String code, String dmpId) throws InternalException;

    List<CustomForm> getFormsSingleTypeVisitWHStatus(String visitId, String userId) throws InternalException;
    List<Warehouse> getWarehouseListByVisitIdAndUserId(String visitId, String userId) throws InternalException;
    Warehouse getWareHouseByVisitIdAndUserIdAndFormId(String visitId, String userId, String formId) throws InternalException;
    List<Form> getAllFormsByDMPIdAndScheduleTypeId(String dmpId, String scheduleTypeId) throws InternalException;
}
