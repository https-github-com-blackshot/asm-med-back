package kz.beeset.med.device.service.implementation;

import kz.beeset.med.admin.model.Profile;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.device.constant.DefaultConstant;
import kz.beeset.med.device.model.DeviceStat;
import kz.beeset.med.device.model.DeviceStatConfig;
import kz.beeset.med.device.repository.DeviceStatConfigRepository;
import kz.beeset.med.device.repository.DeviceStatRepository;
import kz.beeset.med.device.repository.admin.ProfileRepository;
import kz.beeset.med.device.repository.admin.UsersRepository;
import kz.beeset.med.device.service.interfaces.IDeviceStatConfigService;
import kz.beeset.med.device.service.interfaces.IDeviceStatService;
import kz.beeset.med.device.utils.error.ErrorCode;
import kz.beeset.med.device.utils.error.InternalException;
import kz.beeset.med.device.utils.error.InternalExceptionHelper;
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
public class DeviceStatConfigService implements IDeviceStatConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStatConfigService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private IDeviceStatService deviceStatService;
    @Autowired
    private DeviceStatConfigRepository deviceStatConfigRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProfileRepository profileRepository;


    @Override
    public Page<DeviceStatConfig> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DefaultConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
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

            return deviceStatConfigRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DeviceStatConfig> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DeviceStatConfig> readIterable() throws InternalException {
        try {
            return deviceStatConfigRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DeviceStatConfig> readIterable())" +
                    "-", e);
        }
    }

    @Override
    public DeviceStatConfig readByUserId(String userId) throws InternalException {
        try {
            return deviceStatConfigRepository.getByUserId(userId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStatConfig readByUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStatConfig read(String id) throws InternalException {
        try {
            return deviceStatConfigRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStatConfig read(String id)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStatConfig create(DeviceStatConfig deviceStatConfig) throws InternalException {
        try {
            deviceStatConfig.setState(DefaultConstant.STATUS_ACTIVE);

            deviceStatConfig = deviceStatConfigRepository.save(deviceStatConfig);

            DeviceStat deviceStat = deviceStatService.getLastRecordByUserId(deviceStatConfig.getUserId());

            User user = usersRepository.getById(deviceStatConfig.getUserId());
            user.setHealthStatus(deviceStatService.defineHealthStatus(deviceStat));
            user.setState(DefaultConstant.STATUS_ACTIVE);
            usersRepository.save(user);

            Profile profile = profileRepository.getByUserIdAndState(deviceStat.getUserId(), DefaultConstant.STATUS_ACTIVE);
            profile.setHealthStatus(user.getHealthStatus());
            profile.setState(DefaultConstant.STATUS_ACTIVE);
            profileRepository.save(profile);

            return deviceStatConfig;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStatConfig create(DeviceStatConfig deviceStatConfig)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStatConfig update(DeviceStatConfig deviceStatConfig) throws InternalException {
        try {
            deviceStatConfig.setState(DefaultConstant.STATUS_ACTIVE);

            DeviceStat deviceStat = deviceStatService.getLastRecordByUserId(deviceStatConfig.getUserId());

            User user = usersRepository.getById(deviceStatConfig.getUserId());
            user.setHealthStatus(deviceStatService.defineHealthStatus(deviceStat));
            user.setState(DefaultConstant.STATUS_ACTIVE);
            usersRepository.save(user);

            Profile profile = profileRepository.getByUserIdAndState(deviceStat.getUserId(), DefaultConstant.STATUS_ACTIVE);
            profile.setHealthStatus(user.getHealthStatus());
            profile.setState(DefaultConstant.STATUS_ACTIVE);
            profileRepository.save(profile);

            return deviceStatConfigRepository.save(deviceStatConfig);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStatConfig update(DeviceStatConfig deviceStatConfig)" +
                    "-", e);
        }
    }

    @Override
    public DeviceStatConfig delete(String id) throws InternalException {
        try {
            DeviceStatConfig deviceStatConfig = deviceStatConfigRepository.getById(id);

            deviceStatConfig.setState(DefaultConstant.STATUS_DELETED);

            return deviceStatConfigRepository.save(deviceStatConfig);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DeviceStatConfig delete(String id)" +
                    "-", e);
        }
    }
}
