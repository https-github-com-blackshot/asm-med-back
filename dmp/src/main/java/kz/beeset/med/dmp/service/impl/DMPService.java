package kz.beeset.med.dmp.service.impl;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.repository.DMPDoctorRepository;
import kz.beeset.med.dmp.repository.DMPPatientRepository;
import kz.beeset.med.dmp.repository.DMPRepository;
import kz.beeset.med.dmp.service.IDMPService;
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
import java.util.stream.Collectors;

@Service
public class DMPService implements IDMPService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;

    @Override
    public Page<DMP> read(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(DMPConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("nameKz")) {
                query.addCriteria(Criteria.where(DMPConstants.NAMEKZ_FIELD_NAME).is(allRequestParams.get("nameKz")));
            }
            if (allRequestParams.containsKey("nameRu")) {
                query.addCriteria(Criteria.where(DMPConstants.NAMERU_FIELD_NAME).is(allRequestParams.get("nameRu")));
            }
            if (allRequestParams.containsKey("nameEn")) {
                query.addCriteria(Criteria.where(DMPConstants.NAMEEN_FIELD_NAME).is(allRequestParams.get("nameEn")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DMPConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("registrationDate")) {
                query.addCriteria(Criteria.where(DMPConstants.REGISTRATION_DATE_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("registrationDate"))));
            }
            if (allRequestParams.containsKey("category")) {
                query.addCriteria(Criteria.where(DMPConstants.CATEGORY_FIELD_NAME).is(allRequestParams.get("category")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPConstants.STATE_FIELD_NAME).is(DMPConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMP> read(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMP> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPConstants.SORT_DIRECTION_DESC))
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

            return dmpRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMP> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMP> read() throws InternalException {
        try {
            return dmpRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMP> read()" +
                    "-", e);
        }
    }

    @Override
    public List<DMP> readByDoctorUserId(String userId) throws InternalException {
        try {
            List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByUserId(userId);
            List<String> dmpIds = dmpDoctorList.stream().map(DMPDoctor::getDmpId).collect(Collectors.toList());
            return dmpRepository.findAllByIdInAndState(dmpIds, DMPConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMP> readByDoctorUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMP> readByPatientUserId(String userId) throws InternalException {
        try {
            List<DMPPatient> dmpPatientList = dmpPatientRepository.getAllByUserId(userId);
            List<String> dmpIds = dmpPatientList.stream().map(DMPPatient::getDmpId).collect(Collectors.toList());
            return dmpRepository.findAllByIdInAndState(dmpIds, DMPConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMP> readByPatientUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMP> readByCategory(String category) throws InternalException {
        try {
            return dmpRepository.findAllByCategoryAndState(category, DMPConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMP> readByPatientUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public DMP get(String id) throws InternalException {
        try {
            return dmpRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMP get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMP create(DMP dmp) throws InternalException {
        try {
            dmp.setCreatedBy("");
            dmp.setCreatedDate(LocalDateTime.now());
            dmp.setState(DMPConstants.STATUS_ACTIVE);

            return dmpRepository.save(dmp);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMP create(DMP dmp)" +
                    "-", e);
        }
    }

    @Override
    public DMP update(DMP dmp) throws InternalException {
        try {
            dmp.setLastModifiedBy("");
            dmp.setLastModifiedDate(LocalDateTime.now());
            dmp.setState(DMPConstants.STATUS_ACTIVE);

            return dmpRepository.save(dmp);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMP update(DMP dmp)" +
                    "-", e);
        }
    }

    @Override
    public DMP delete(String id) throws InternalException {
        try {
            DMP dmp = dmpRepository.getById(id);

            dmp.setLastModifiedBy("");
            dmp.setLastModifiedDate(LocalDateTime.now());

            dmp.setState(DMPConstants.STATUS_DELETED);

            return dmpRepository.save(dmp);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMP delete(String id)" +
                    "-", e);
        }
    }

}
