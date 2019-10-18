package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.constant.DMPScheduleSubTypeConstant;
import kz.beeset.med.dmp.model.DMPScheduleType;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPScheduleTypeService {

    Page<DMPScheduleType> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPScheduleType> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPScheduleType> readIterableByDMPId(String dmpId) throws InternalException;
    DMPScheduleType get(String id) throws InternalException;
    DMPScheduleType create(DMPScheduleType dmpScheduleType) throws InternalException;
    DMPScheduleType update(DMPScheduleType dmpScheduleType) throws InternalException;
    DMPScheduleType delete(String id) throws InternalException;
    List<DMPScheduleSubTypeConstant.DMPScheduleSubTypeObject> getDMPScheduleTypeSubTypes() throws InternalException;
    DMPScheduleSubTypeConstant.DMPScheduleSubTypeObject getDMPScheduleTypeSubTypeByCode(String code) throws InternalException;
}
