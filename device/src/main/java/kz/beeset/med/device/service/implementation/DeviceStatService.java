package kz.beeset.med.device.service.implementation;

import kz.beeset.med.admin.model.Profile;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.device.constant.DefaultConstant;
import kz.beeset.med.device.model.DeviceStat;
import kz.beeset.med.device.model.DeviceStatConfig;
import kz.beeset.med.device.model.DeviceStatConfigObjValue;
import kz.beeset.med.device.repository.DeviceStatConfigRepository;
import kz.beeset.med.device.repository.DeviceStatRepository;
import kz.beeset.med.device.repository.admin.ProfileRepository;
import kz.beeset.med.device.repository.admin.UsersRepository;
import kz.beeset.med.device.service.interfaces.IDeviceStatService;
import kz.beeset.med.device.utils.error.ErrorCode;
import kz.beeset.med.device.utils.error.InternalException;
import kz.beeset.med.device.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DeviceStatService implements IDeviceStatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStatService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DeviceStatRepository deviceStatRepository;
    @Autowired
    private DeviceStatConfigRepository deviceStatConfigRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Page<DeviceStat> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where("id").is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("checkDate")) {
                query.addCriteria(Criteria.where("checkDate").is(allRequestParams.get("checkDate")));
            }
            if (allRequestParams.containsKey("deviceName")) {
                query.addCriteria(Criteria.where("deviceName").is(allRequestParams.get("deviceName")));
            }
            if (allRequestParams.containsKey("userId")) {
                query.addCriteria(Criteria.where("userId").is(allRequestParams.get("userId")));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DefaultConstant.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DefaultConstant.STATE_FIELD_NAME).is(DefaultConstant.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return deviceStatRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DeviceStat> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DeviceStat> search(Map<String, String> allRequestParams) throws InternalException {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DefaultConstant.SORT_DIRECTION_DESC))
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
                    "Page<DeviceStat> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DeviceStat> readIterableByUserId(String userId) throws InternalException {
        try {
            return deviceStatRepository.getAllByUserId(userId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DeviceStat> readIterableByUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStat getLastRecordByUserId(String userId) throws InternalException {
        try {
                return deviceStatRepository.findAllByUserIdSortedByCreatedDate(userId, new Sort(Sort.Direction.DESC, "createdDate")).get(0);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStat getLastRecordByUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStat get(String id) throws InternalException {
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
    public DeviceStat create(DeviceStat deviceStat) throws InternalException {
        try {
            deviceStat.setState(DefaultConstant.STATUS_ACTIVE);

            User user = usersRepository.getById(deviceStat.getUserId());
            user.setHealthStatus(defineHealthStatus(deviceStat));
            user.setState(DefaultConstant.STATUS_ACTIVE);
            usersRepository.save(user);

            Profile profile = profileRepository.getByUserIdAndState(deviceStat.getUserId(), DefaultConstant.STATUS_ACTIVE);
            profile.setHealthStatus(user.getHealthStatus());
            profile.setState(DefaultConstant.STATUS_ACTIVE);
            profileRepository.save(profile);

            return deviceStatRepository.save(deviceStat);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStat create(DeviceStat deviceStat)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStat update(DeviceStat deviceStat) throws InternalException {
        try {
            deviceStat.setCheckDate(deviceStat.getCheckDate().plusHours(6));
            deviceStat.setState(DefaultConstant.STATUS_ACTIVE);

            User user = usersRepository.getById(deviceStat.getUserId());
            user.setHealthStatus(defineHealthStatus(deviceStat));
            user.setState(DefaultConstant.STATUS_ACTIVE);
            usersRepository.save(user);

            Profile profile = profileRepository.getByUserIdAndState(deviceStat.getUserId(), DefaultConstant.STATUS_ACTIVE);
            profile.setHealthStatus(user.getHealthStatus());
            profile.setState(DefaultConstant.STATUS_ACTIVE);
            profileRepository.save(profile);

            return deviceStatRepository.save(deviceStat);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStat update(DeviceStat deviceStat)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStat delete(String id) throws InternalException {
        try {
            DeviceStat dmpDeviceStat = deviceStatRepository.getById(id);

            dmpDeviceStat.setLastModifiedBy("");
            dmpDeviceStat.setLastModifiedDate(LocalDateTime.now());

            dmpDeviceStat.setState(DefaultConstant.STATUS_DELETED);

            return deviceStatRepository.save(dmpDeviceStat);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDeviceStat delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public int defineHealthStatus(DeviceStat deviceStat) throws InternalException {

        if (deviceStat == null) {
            return 6; // Устройство не зарегистрировано
        }

        if (deviceStat.getId() != null) {
            Map<String, Object> parameters = deviceStat.getParameterMap();

            AtomicBoolean hasValue = new AtomicBoolean(false);

            parameters.forEach((s, o) -> {
                if (o != null) hasValue.set(true);
            });

            if (!hasValue.get()) {
                return 5; // Нет данных о браслете;
            }
        }

        DeviceStatConfig deviceStatConfig = deviceStatConfigRepository.getByUserId(deviceStat.getUserId());

        AtomicInteger healthStatus = new AtomicInteger();

        if (deviceStatConfig != null) {
            AtomicBoolean everythingIsGood = new AtomicBoolean(true);
            AtomicBoolean somethingWrong = new AtomicBoolean(false);
            AtomicBoolean maybeWrongInTheFuture = new AtomicBoolean(false);

            Map<String, DeviceStatConfigObjValue> parametersMap = deviceStatConfig.getParameterMap();
            parametersMap.keySet().forEach(parameter -> {
                DeviceStatConfigObjValue referenceValues = parametersMap.get(parameter);
                Object valueFromDevice = deviceStat.getParameterMap().get(parameter);

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
}
