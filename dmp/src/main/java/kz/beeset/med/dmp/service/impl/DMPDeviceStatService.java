package kz.beeset.med.dmp.service.impl;

import kz.beeset.med.dmp.constant.DMPDeviceStatConstants;
import kz.beeset.med.dmp.constant.DMPPatientConstants;
import kz.beeset.med.dmp.model.DMPDeviceStat;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPDeviceStatConfigObjValue;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.repository.DMPDeviceStatConfigRepository;
import kz.beeset.med.dmp.repository.DMPDeviceStatRepository;
import kz.beeset.med.dmp.repository.DMPPatientRepository;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DMPDeviceStatService implements IDMPDeviceStatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPDeviceStatService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPDeviceStatRepository deviceStatRepository;
    @Autowired
    private DMPDeviceStatConfigRepository dmpDeviceStatConfigRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;

    @Override
    public Page<DMPDeviceStat> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPDeviceStatConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPDeviceStatConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPDeviceStatConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("checkDate")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConstants.CHECK_DATE_FIELD_NAME).is(allRequestParams.get("checkDate")));
            }
            if (allRequestParams.containsKey("deviceInfo")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConstants.DEVICE_INFO_FIELD_NAME).is(allRequestParams.get("deviceInfo")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("dmpPatientId")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConstants.DMP_PATIENT_ID_FIELD_NAME).is(allRequestParams.get("dmpPatientId")));
            }
            if (allRequestParams.containsKey("dmpPatientUserId")) {
                query.addCriteria(Criteria.where(DMPDeviceStatConstants.DMP_PATIENT_USER_ID_FIELD_NAME).is(allRequestParams.get("dmpPatientUserId")));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPDeviceStatConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPDeviceStatConstants.STATE_FIELD_NAME).is(DMPDeviceStatConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return deviceStatRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDeviceStat> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPDeviceStat> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPDeviceStatConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPDeviceStatConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPDeviceStatConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPDeviceStatConstants.SORT_DIRECTION_DESC))
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

            return deviceStatRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDeviceStat> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPDeviceStat> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return deviceStatRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDeviceStat> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStat getLastRecordByDMPPatientId(String dmpPatientId) throws InternalException {
        try {
            return deviceStatRepository.findAllByDMPPatientIdSortedByCreatedDate(dmpPatientId, new Sort(Sort.Direction.DESC, "createdDate")).get(0);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStat getLastRecordByDMPPatientId(String dmpPatientId)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStat get(String id) throws InternalException {
        try {
            return deviceStatRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStat get(String id)" +
                    "-", e);
        }
    }

    @Override
    public int defineHealthStatus(DMPDeviceStat dmpDeviceStat) throws InternalException {

        if (dmpDeviceStat == null) {
            return 6; // Устройство не зарегистрировано
        }

        if (dmpDeviceStat.getId() != null ) {
            Map<String, Object> parameters = dmpDeviceStat.getParameterMap();

            AtomicBoolean hasValue = new AtomicBoolean(false);

            parameters.forEach((s, o) -> {
                if (o != null) hasValue.set(true);
            });

            if (!hasValue.get()){
                return 5; // Нет данныз с браслета
            }
        }

        DMPDeviceStatConfig dmpDeviceStatConfig = dmpDeviceStatConfigRepository.getByDMPPatientId(dmpDeviceStat.getDmpPatientId());

        AtomicInteger healthStatus = new AtomicInteger();

        if (dmpDeviceStatConfig != null) {
            AtomicBoolean everythingIsGood = new AtomicBoolean(true);
            AtomicBoolean somethingWrong = new AtomicBoolean(false);
            AtomicBoolean maybeWrongInTheFuture = new AtomicBoolean(false);

            Map<String, DMPDeviceStatConfigObjValue> parametersMap = dmpDeviceStatConfig.getParameterMap();
            parametersMap.keySet().forEach(parameter -> {
                DMPDeviceStatConfigObjValue referenceValues = parametersMap.get(parameter);
                Object valueFromDevice = dmpDeviceStat.getParameterMap().get(parameter);

                if (referenceValues.isActive() && valueFromDevice != null) {
                    if (valueFromDevice instanceof Integer) {
                        Integer referenceValue1 = (Integer) referenceValues.getValue1();
                        Integer referenceValue2 = (Integer) referenceValues.getValue2();
                        Integer value = (Integer) valueFromDevice;

                        Integer differenceWithMax = (Integer) referenceValues.getValue2() - (Integer) valueFromDevice;
                        Integer differenceWithMin = (Integer) valueFromDevice - (Integer) referenceValues.getValue1();

                        Integer differenceBetweenMinAndMax = (Integer) referenceValues.getValue2() - (Integer) referenceValues.getValue1();
                        Double twentyPercentAllowable = differenceBetweenMinAndMax * 0.2;

                        if (value < referenceValue1 || value > referenceValue2) {
                            everythingIsGood.set(false);
                            somethingWrong.set(true);
                        } else {
                            if (differenceWithMax <= twentyPercentAllowable || differenceWithMin <= twentyPercentAllowable) {
                                everythingIsGood.set(false);
                                maybeWrongInTheFuture.set(true);
                            }
                        }
                    } else if (valueFromDevice instanceof Double) {
                        Double referenceValue1 = (Double) referenceValues.getValue1();
                        Double referenceValue2 = (Double) referenceValues.getValue2();
                        Double value = (Double) valueFromDevice;

                        Double differenceWithMax = (Double) referenceValues.getValue2() - (Double) valueFromDevice;
                        Double differenceWithMin = (Double) valueFromDevice - (Double) referenceValues.getValue1();

                        Double differenceBetweenMinAndMax = (Double) referenceValues.getValue2() - (Double) referenceValues.getValue1();
                        Double twentyPercentAllowable = differenceBetweenMinAndMax * 0.2;

                        if (value < referenceValue1 || value > referenceValue2) {
                            everythingIsGood.set(false);
                            somethingWrong.set(true);
                        } else {
                            if (differenceWithMax <= twentyPercentAllowable || differenceWithMin <= twentyPercentAllowable) {
                                everythingIsGood.set(false);
                                maybeWrongInTheFuture.set(true);
                            }
                        }
                    }
                }

                if (everythingIsGood.get()) {
                    healthStatus.set(3); // Показание в норме;
                } else if (!somethingWrong.get() && maybeWrongInTheFuture.get()) {
                    healthStatus.set(2); // Показания в пределах границ
                } else {
                    healthStatus.set(1); // Показания отклонены от нормы;
                }
            });
        } else {
            healthStatus.set(4); // Устройство не настроено;
        }

        return healthStatus.get();
    }

    @Override
    public DMPDeviceStat create(DMPDeviceStat dmpDeviceStat) throws InternalException {
        try {
            LOGGER.debug("-----------------------------------------------------");
            LOGGER.debug(dmpDeviceStat.getCheckDate().toString());
//            LocalDateTime checkDate = dmpDeviceStat.getCheckDate().plusHours(6)
//            dmpDeviceStat.setCheckDate();
            LOGGER.debug(dmpDeviceStat.getCheckDate().toString());
            dmpDeviceStat.setCreatedBy("");
            dmpDeviceStat.setCreatedDate(LocalDateTime.now());
            dmpDeviceStat.setState(DMPDeviceStatConstants.STATUS_ACTIVE);

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStat.getDmpPatientId());
            dmpPatient.setHealthStatus(defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatientRepository.save(dmpPatient);

            return deviceStatRepository.save(dmpDeviceStat);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStat create(DMPDeviceStat dmpDeviceStat)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStat update(DMPDeviceStat dmpDeviceStat) throws InternalException {
        try {
            dmpDeviceStat.setCheckDate(dmpDeviceStat.getCheckDate().plusHours(6));
            dmpDeviceStat.setLastModifiedBy("");
            dmpDeviceStat.setLastModifiedDate(LocalDateTime.now());
            dmpDeviceStat.setState(DMPDeviceStatConstants.STATUS_ACTIVE);

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStat.getDmpPatientId());
            dmpPatient.setHealthStatus(defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatientRepository.save(dmpPatient);

            return deviceStatRepository.save(dmpDeviceStat);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStat update(DMPDeviceStat dmpDeviceStat)" +
                    "-", e);
        }
    }

    @Override
    public DMPDeviceStat delete(String id) throws InternalException {
        try {
            DMPDeviceStat dmpDeviceStat = deviceStatRepository.getById(id);

            dmpDeviceStat.setLastModifiedBy("");
            dmpDeviceStat.setLastModifiedDate(LocalDateTime.now());

            dmpDeviceStat.setState(DMPDeviceStatConstants.STATUS_DELETED);

            return deviceStatRepository.save(dmpDeviceStat);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStat delete(String id)" +
                    "-", e);
        }
    }
}
