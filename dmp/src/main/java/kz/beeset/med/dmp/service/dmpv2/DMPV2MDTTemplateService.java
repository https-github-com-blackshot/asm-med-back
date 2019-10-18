package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.constant.DMPMDTTemplateConstants;
import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTTemplate;
import kz.beeset.med.dmp.repository.dmpv2.DMPV2MDTTemplateRepository;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DMPV2MDTTemplateService  implements IDMPV2MDTTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2MDTTemplateService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPV2MDTTemplateRepository dmpmdtTemplateRepository;

    @Override
    public Page<DMPV2MDTTemplate> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPMDTTemplateConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPMDTTemplateConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPMDTTemplateConstants.ID_FIELD_NAME;

            String dmpId = "";

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPMDTTemplateConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(DMPMDTTemplateConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPMDTTemplateConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("ownerId")) {
                query.addCriteria(Criteria.where(DMPMDTTemplateConstants.OWNER_ID_FIELD_NAME).is(allRequestParams.get("ownerId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPMDTTemplateConstants.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;

            }
            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }

            query.addCriteria(Criteria.where(DMPMDTTemplateConstants.STATE_FIELD_NAME).is(DMPMDTTemplateConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpmdtTemplateRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2MDTTemplate> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPV2MDTTemplate> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPMDTTemplateConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPMDTTemplateConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPMDTTemplateConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPMDTTemplateConstants.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;

            }
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

            return dmpmdtTemplateRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2MDTTemplate> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2MDTTemplate> readIterableByDiseaseId(String dmpId) throws InternalException {
        try {
            return dmpmdtTemplateRepository.getAllByDiseaseId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDeviceStatConfig> readIterableByDiseaseId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2MDTTemplate get(String id) throws InternalException {
        try {
            return dmpmdtTemplateRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStatConfig get(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2MDTTemplate> getByUserId(String id) throws InternalException {
        try {
            return dmpmdtTemplateRepository.getByUserId(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2MDTTemplate List<DMPV2MDTTemplate> getByUserID" +
                    "-", e);
        }
    }

    @Override
    public DMPV2MDTTemplate create(DMPV2MDTTemplate DMPV2MDTTemplate) throws InternalException {
        try {
            DMPV2MDTTemplate.setCreatedBy("");
            DMPV2MDTTemplate.setCreatedDate(LocalDateTime.now());
            DMPV2MDTTemplate.setState(DMPMDTTemplateConstants.STATUS_ACTIVE);

            return dmpmdtTemplateRepository.save(DMPV2MDTTemplate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2MDTTemplate create(DMPV2MDTTemplate DMPV2MDTTemplate)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2MDTTemplate update(DMPV2MDTTemplate DMPV2MDTTemplate) throws InternalException {
        try {
            DMPV2MDTTemplate.setLastModifiedBy("");
            DMPV2MDTTemplate.setLastModifiedDate(LocalDateTime.now());
            DMPV2MDTTemplate.setState(DMPMDTTemplateConstants.STATUS_ACTIVE);

            return dmpmdtTemplateRepository.save(DMPV2MDTTemplate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2MDTTemplate update(DMPV2MDTTemplate DMPV2MDTTemplate)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2MDTTemplate delete(String id) throws InternalException {
        try {
            DMPV2MDTTemplate DMPV2MDTTemplate = dmpmdtTemplateRepository.getById(id);

            DMPV2MDTTemplate.setLastModifiedBy("");
            DMPV2MDTTemplate.setLastModifiedDate(LocalDateTime.now());

            DMPV2MDTTemplate.setState(DMPMDTTemplateConstants.STATUS_DELETED);

            return dmpmdtTemplateRepository.save(DMPV2MDTTemplate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2MDTTemplate delete(String id)" +
                    "-", e);
        }
    }
}