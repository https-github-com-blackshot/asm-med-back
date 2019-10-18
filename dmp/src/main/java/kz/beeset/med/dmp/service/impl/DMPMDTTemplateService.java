package kz.beeset.med.dmp.service.impl;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPDeviceStatConfigConstants;
import kz.beeset.med.dmp.constant.DMPMDTTemplateConstants;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.DMPMDTTemplate;
import kz.beeset.med.dmp.repository.DMPMDTTemplateRepository;
import kz.beeset.med.dmp.service.IDMPMDTTemplateService;
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
public class DMPMDTTemplateService implements IDMPMDTTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPMDTTemplateService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPMDTTemplateRepository dmpmdtTemplateRepository;

    @Override
    public Page<DMPMDTTemplate> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
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
                    "Page<DMPMDTTemplate> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPMDTTemplate> search(Map<String, String> allRequestParams) throws InternalException {
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
                    "Page<DMPMDTTemplate> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPMDTTemplate> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpmdtTemplateRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDeviceStatConfig> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTTemplate get(String id) throws InternalException {
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
    public List<DMPMDTTemplate> getByUserId(String id) throws InternalException {
        try {
            return dmpmdtTemplateRepository.getByUserId(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTTemplate List<DMPMDTTemplate> getByUserID" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTTemplate create(DMPMDTTemplate dmpmdtTemplate) throws InternalException {
        try {
            dmpmdtTemplate.setCreatedBy("");
            dmpmdtTemplate.setCreatedDate(LocalDateTime.now());
            dmpmdtTemplate.setState(DMPMDTTemplateConstants.STATUS_ACTIVE);

            return dmpmdtTemplateRepository.save(dmpmdtTemplate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTTemplate create(DMPMDTTemplate dmpmdtTemplate)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTTemplate update(DMPMDTTemplate dmpmdtTemplate) throws InternalException {
        try {
            dmpmdtTemplate.setLastModifiedBy("");
            dmpmdtTemplate.setLastModifiedDate(LocalDateTime.now());
            dmpmdtTemplate.setState(DMPMDTTemplateConstants.STATUS_ACTIVE);

            return dmpmdtTemplateRepository.save(dmpmdtTemplate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTTemplate update(DMPMDTTemplate dmpmdtTemplate)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTTemplate delete(String id) throws InternalException {
        try {
            DMPMDTTemplate dmpmdtTemplate = dmpmdtTemplateRepository.getById(id);

            dmpmdtTemplate.setLastModifiedBy("");
            dmpmdtTemplate.setLastModifiedDate(LocalDateTime.now());

            dmpmdtTemplate.setState(DMPMDTTemplateConstants.STATUS_DELETED);

            return dmpmdtTemplateRepository.save(dmpmdtTemplate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTTemplate delete(String id)" +
                    "-", e);
        }
    }
}
