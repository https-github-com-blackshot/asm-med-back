package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPDeviceStat;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPDeviceStatService {

    Page<DMPDeviceStat> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPDeviceStat> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPDeviceStat> readIterableByDMPId(String dmpId) throws InternalException;
    DMPDeviceStat getLastRecordByDMPPatientId(String dmpPatientId) throws InternalException;
    DMPDeviceStat get(String id) throws InternalException;
    DMPDeviceStat create(DMPDeviceStat dmpDeviceStat) throws InternalException;
    DMPDeviceStat update(DMPDeviceStat dmpDeviceStat) throws InternalException;
    DMPDeviceStat delete(String id) throws InternalException;
    int defineHealthStatus(DMPDeviceStat dmpDeviceStat) throws InternalException;

}
