package kz.beeset.med.admin.service;

import java.util.List;
import java.util.Map;

import kz.beeset.med.admin.model.catalog.Catalog;
import kz.beeset.med.admin.model.catalog.City;
import kz.beeset.med.admin.model.catalog.PAEventType;
import kz.beeset.med.admin.model.catalog.PAStatus;
import kz.beeset.med.admin.utils.error.InternalException;
import org.bouncycastle.crypto.params.ParametersWithSalt;
import org.springframework.data.domain.Page;

public interface ICatalogService {
    Page<Catalog> getCatalogList(Map<String, String> allRequestParams) throws InternalException;
    Catalog getCatalogById(String id) throws InternalException;
    Catalog setCatalog(Catalog catalog) throws InternalException;
    Catalog getCatalogByCode(String code) throws InternalException;
    void deleteCatalogById(String id) throws InternalException;

    List<PAEventType> getEventTypeList() throws InternalException;
    PAEventType getEventTypeById(String id) throws InternalException;
    PAEventType setEventType(PAEventType eventType) throws InternalException;
    void deleteEventTypeById(String id) throws InternalException;

    List<PAStatus> getStatusList() throws InternalException;
    PAStatus getStatusById(String id) throws InternalException;
    PAStatus setStatus(PAStatus status) throws InternalException;
    void deleteStatusById(String id) throws InternalException;

    List<City> getCityList() throws InternalException;
    City getCityById(String id) throws InternalException;
    City setCity(City city) throws InternalException;
    void deleteCityById(String id) throws InternalException;

}