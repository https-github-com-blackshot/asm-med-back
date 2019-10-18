package kz.beeset.med.dmp.service.impl;

import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPDeviceStatConfigConstants;
import kz.beeset.med.dmp.constant.DMPPatientConstants;
import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPDeviceStat;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.repository.DMPDeviceStatConfigRepository;
import kz.beeset.med.dmp.repository.DMPPatientRepository;
import kz.beeset.med.dmp.service.IDMPDeviceStatConfigService;
import kz.beeset.med.dmp.service.IDMPDeviceStatService;
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
public class DMPDeviceStatConfigService implements IDMPDeviceStatConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPDeviceStatConfigService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPDeviceStatConfigRepository dmpDeviceStatConfigRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;
    @Autowired
    private IDMPDeviceStatService dmpDeviceStatService;

    @Override
    public Page<DMPDeviceStatConfig> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPDeviceStatConfigConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPDeviceStatConfigConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPDeviceStatConfigConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConfigConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConfigConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("dmpPatientId")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConfigConstants.DMP_PATIENT_ID_FIELD_NAME).is(allRequestParams.get("dmpPatientId")));
            }
            if (allRequestParams.containsKey("dmpPatientUserId")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConfigConstants.DMP_PATIENT_USER_ID_FIELD_NAME).is(allRequestParams.get("dmpPatientUserId")));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPDeviceStatConfigConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPDeviceStatConfigConstants.STATE_FIELD_NAME).is(DMPDeviceStatConfigConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpDeviceStatConfigRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDeviceStatConfig> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPDeviceStatConfig> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpDeviceStatConfigRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDeviceStatConfig> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStatConfig get(String id) throws InternalException {
        try {
            return dmpDeviceStatConfigRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStatConfig get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStatConfig create(DMPDeviceStatConfig dmpDeviceStatConfig) throws InternalException {
        try {
            dmpDeviceStatConfig.setCreatedBy("");
            dmpDeviceStatConfig.setCreatedDate(LocalDateTime.now());
            dmpDeviceStatConfig.setState(DMPDeviceStatConfigConstants.STATUS_ACTIVE);

            DMPDeviceStat dmpDeviceStat = dmpDeviceStatService.getLastRecordByDMPPatientId(dmpDeviceStatConfig.getDmpPatientId());

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStatConfig.getDmpPatientId());
            dmpPatient.setHealthStatus(dmpDeviceStatService.defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatientRepository.save(dmpPatient);

            return dmpDeviceStatConfigRepository.save(dmpDeviceStatConfig);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStatConfig create(DMPDeviceStatConfig dmpDeviceStatConfig)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStatConfig update(DMPDeviceStatConfig dmpDeviceStatConfig) throws InternalException {
        try {
            dmpDeviceStatConfig.setLastModifiedBy("");
            dmpDeviceStatConfig.setLastModifiedDate(LocalDateTime.now());
            dmpDeviceStatConfig.setState(DMPDeviceStatConfigConstants.STATUS_ACTIVE);

            DMPDeviceStat dmpDeviceStat = dmpDeviceStatService.getLastRecordByDMPPatientId(dmpDeviceStatConfig.getDmpPatientId());

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStatConfig.getDmpPatientId());
            dmpPatient.setHealthStatus(dmpDeviceStatService.defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatientRepository.save(dmpPatient);

            return dmpDeviceStatConfigRepository.save(dmpDeviceStatConfig);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStatConfig update(DMPDeviceStatConfig dmpDeviceStatConfig)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStatConfig delete(String id) throws InternalException {
        try {
            DMPDeviceStatConfig dmpDeviceStatConfig = dmpDeviceStatConfigRepository.getById(id);

            dmpDeviceStatConfig.setLastModifiedBy("");
            dmpDeviceStatConfig.setLastModifiedDate(LocalDateTime.now());

            dmpDeviceStatConfig.setState(DMPDeviceStatConfigConstants.STATUS_DELETED);

            return dmpDeviceStatConfigRepository.save(dmpDeviceStatConfig);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStatConfig delete(String id)" +
                    "-", e);
        }
    }
}
