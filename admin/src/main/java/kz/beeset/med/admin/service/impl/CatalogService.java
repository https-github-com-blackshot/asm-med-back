package kz.beeset.med.admin.service.impl;

import java.util.List;
import java.util.Map;

import kz.beeset.med.admin.model.catalog.City;
import kz.beeset.med.admin.model.catalog.PAEventType;
import kz.beeset.med.admin.model.catalog.PAStatus;
import kz.beeset.med.admin.repository.CityRepository;
import kz.beeset.med.admin.repository.PAEventTypeRepository;
import kz.beeset.med.admin.repository.PAStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kz.beeset.med.admin.model.catalog.Catalog;
import kz.beeset.med.admin.repository.CatalogRepository;
import kz.beeset.med.admin.service.ICatalogService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;

@Service
public class CatalogService implements ICatalogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    CatalogRepository catalogRepository;
    @Autowired
    PAEventTypeRepository eventTypeRepository;
    @Autowired
    PAStatusRepository statusRepository;
    @Autowired
    CityRepository cityRepository;

    @Override
    public Page<Catalog> getCatalogList(Map<String, String> allRequestParams) throws InternalException {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = 0;

            int pageSize = 5;

            String sortBy = "id";

            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            Page<Catalog> catalogPage = this.catalogRepository.findAll(pageableRequest);

            return catalogPage;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getCatalogList", e);
        }
    }

    @Override
    public Catalog getCatalogById(String id) throws InternalException {
        try {
            return  catalogRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getCatalogById id: " + id, e);
        }
    }

    @Override
    public Catalog setCatalog(Catalog catalog) throws InternalException {
        try {
            if(catalogRepository.getCatalogByUrlCode(catalog.getUrlCode()) != null){
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setCatalog urlCode [DUPLICATE]");
            }
            return catalogRepository.save(catalog);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setCatalog ", e);
        }
    }

    @Override
    public Catalog getCatalogByCode(String code) throws InternalException {
        try {
            return catalogRepository.getCatalogByUrlCode(code);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getCatalogByCode code: " + code, e);
        }
    }

    @Override
    public void deleteCatalogById(String id) throws InternalException {
        try {
            this.catalogRepository.deleteCatalogById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:deleteCatalogById ", e);
        }
    }

    @Override
    public List<PAEventType> getEventTypeList() throws InternalException {
        try {
            return eventTypeRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getEventTypeList ", e);
        }
    }

    @Override
    public PAEventType getEventTypeById(String id) throws InternalException {
        try {
            return  eventTypeRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getEventTypeById id: " + id, e);
        }
    }

    @Override
    public PAEventType setEventType(PAEventType eventType) throws InternalException {
        try {
            return  eventTypeRepository.save(eventType);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setEventType ", e);
        }
    }

    @Override
    public void deleteEventTypeById(String id) throws InternalException {
        try {
            this.eventTypeRepository.deleteEventTypeById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:deleteEventTypeById ", e);
        }
    }

    @Override
    public List<PAStatus> getStatusList() throws InternalException {
        try {
            return statusRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getStatusList ", e);
        }
    }

    @Override
    public PAStatus getStatusById(String id) throws InternalException {
        try {
            return  statusRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getStatusById id: " + id, e);
        }
    }

    @Override
    public PAStatus setStatus(PAStatus status) throws InternalException {
        try {
            return  statusRepository.save(status);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setStatus ", e);
        }
    }

    @Override
    public void deleteStatusById(String id) throws InternalException {
        try {
            this.statusRepository.deleteStatusById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:deleteStatusById ", e);
        }
    }

    @Override
    public List<City> getCityList() throws InternalException {
        try {
            return cityRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getCityList ", e);
        }
    }

    @Override
    public City getCityById(String id) throws InternalException {
        try {
            return  cityRepository.getCityById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getCityById id: " + id, e);
        }
    }

    @Override
    public City setCity(City city) throws InternalException {
        try {
            return  cityRepository.save(city);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setCity ", e);
        }
    }

    @Override
    public void deleteCityById(String id) throws InternalException {
        try {
            this.cityRepository.deleteCityById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:deleteCityById ", e);
        }
    }
}