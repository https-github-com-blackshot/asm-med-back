package kz.beeset.med.dmp.service.impl;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPDeviceStatConfigConstants;
import kz.beeset.med.dmp.constant.DMPScheduleSubTypeConstant;
import kz.beeset.med.dmp.constant.DMPScheduleTypeConstants;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPScheduleType;
import kz.beeset.med.dmp.repository.DMPScheduleTypeRepository;
import kz.beeset.med.dmp.service.IDMPScheduleTypeService;
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
public class DMPScheduleTypeService implements IDMPScheduleTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPScheduleTypeService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPScheduleTypeRepository dmpScheduleTypeRepository;

    @Override
    public Page<DMPScheduleType> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPScheduleTypeConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPScheduleTypeConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPScheduleTypeConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("nameKz")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.NAMEKZ_FIELD_NAME).is(allRequestParams.get("nameKz")));
            }
            if (allRequestParams.containsKey("nameRu")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.NAMERU_FIELD_NAME).is(allRequestParams.get("nameRu")));
            }
            if (allRequestParams.containsKey("nameEn")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.NAMEEN_FIELD_NAME).is(allRequestParams.get("nameEn")));
            }
            if (allRequestParams.containsKey("type")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.TYPE_FIELD_NAME).is(allRequestParams.get("type")));
            }
            if (allRequestParams.containsKey("visitCount")) {
                query.addCriteria(Criteria.where(DMPScheduleTypeConstants.VISIT_COUNT_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("visitCount"))));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPScheduleTypeConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPScheduleTypeConstants.STATE_FIELD_NAME).is(DMPScheduleTypeConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpScheduleTypeRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPScheduleType> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPScheduleType> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPScheduleTypeConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPScheduleTypeConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPScheduleTypeConstants.ID_FIELD_NAME;

            String dmpId = "";

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPScheduleTypeConstants.SORT_DIRECTION_DESC))
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
            if (allRequestParams.containsKey("dmpId")) {
                dmpId = allRequestParams.get("dmpId");
            }else{
                LOGGER.error("[DMPScheduleTypeService] DMP ID not set in search(Map<String,String> allRequestParams)!");
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                        "DMPScheduleType create(DMPScheduleType dmpScheduleType)" +
                        "- DMP ID NOT SET");
            }
            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpScheduleTypeRepository.query(allRequestParams.get("searchString"), dmpId, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPScheduleType> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPScheduleType> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpScheduleTypeRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPScheduleType> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }


    @Override
    public List<DMPScheduleSubTypeConstant.DMPScheduleSubTypeObject> getDMPScheduleTypeSubTypes() throws InternalException {
        try {
            return DMPScheduleSubTypeConstant.getList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPScheduleTypeService getScheduleTypeSubTypes(): ", e);
        }
    }

    @Override
    public DMPScheduleSubTypeConstant.DMPScheduleSubTypeObject getDMPScheduleTypeSubTypeByCode(String code) throws InternalException {
        try {
            final Map<String, DMPScheduleSubTypeConstant.DMPScheduleSubTypeObject> map = DMPScheduleSubTypeConstant.getMap();
            if (map.containsKey(code))
                return map.get(code);
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPScheduleTypeService getDMPScheduleTypeSubTypeByCode(String code): {" +
                            "\ncode: " + code + "\n}", e);
        }
    }
    @Override
    public DMPScheduleType get(String id) throws InternalException {
        try {
            return dmpScheduleTypeRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPScheduleType get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPScheduleType create(DMPScheduleType dmpScheduleType) throws InternalException {
        try {
            dmpScheduleType.setCreatedBy("");
            dmpScheduleType.setCreatedDate(LocalDateTime.now());
            dmpScheduleType.setState(DMPScheduleTypeConstants.STATUS_ACTIVE);

            return dmpScheduleTypeRepository.save(dmpScheduleType);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPScheduleType create(DMPScheduleType dmpScheduleType)" +
                    "-", e);
        }
    }

    @Override
    public DMPScheduleType update(DMPScheduleType dmpScheduleType) throws InternalException {
        try {
            dmpScheduleType.setLastModifiedBy("");
            dmpScheduleType.setLastModifiedDate(LocalDateTime.now());
            dmpScheduleType.setState(DMPScheduleTypeConstants.STATUS_ACTIVE);

            return dmpScheduleTypeRepository.save(dmpScheduleType);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPScheduleType update(DMPScheduleType dmpScheduleType)" +
                    "-", e);
        }
    }

    @Override
    public DMPScheduleType delete(String id) throws InternalException {
        try {
            DMPScheduleType dmpScheduleType = dmpScheduleTypeRepository.getById(id);

            dmpScheduleType.setLastModifiedBy("");
            dmpScheduleType.setLastModifiedDate(LocalDateTime.now());

            dmpScheduleType.setState(DMPScheduleTypeConstants.STATUS_DELETED);

            return dmpScheduleTypeRepository.save(dmpScheduleType);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPScheduleType delete(String id)" +
                    "-", e);
        }
    }
}
