package kz.beeset.med.admin.service.impl;

import com.netflix.discovery.converters.Auto;
import kz.beeset.med.admin.constant.DefaultConstant;
import kz.beeset.med.admin.constant.UserConstants;
import kz.beeset.med.admin.model.Profile;
import kz.beeset.med.admin.model.Role;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.repository.ProfileRepository;
import kz.beeset.med.admin.service.IProfileService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
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

import java.util.List;
import java.util.Map;

@Service
public class ProfileService implements IProfileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UsersService usersService;


    @Override
    public Page<Profile> readProfilesPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = UserConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = UserConstants.DEFAUT_PAGE_SIZE;

            String sortBy = UserConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(UserConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }

            if (allRequestParams.containsKey("userId")) {
                query.addCriteria(Criteria.where("userId").is(allRequestParams.get("userId")));
            }
            if (allRequestParams.containsKey("healthStatus")) {
                query.addCriteria(Criteria.where("healthStatus").is(Integer.parseInt(allRequestParams.get("healthStatus"))));
            }

            if (allRequestParams.containsKey("doctor")) {
                query.addCriteria(Criteria.where("doctors").in(allRequestParams.get("doctor")));
            }

            if (allRequestParams.containsKey("state")) {
                query.addCriteria(Criteria.where("state").is(Integer.parseInt(allRequestParams.get("state"))));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(UserConstants.SORT_DIRECTION_DESC))
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

            Page<Profile> profiles = profileRepository.findAll(query, pageableRequest);

            return profiles;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:ProfileService getById", e);
        }
    }

    @Override
    public Profile getById(String id) throws InternalException {
        try {
            return profileRepository.getByIdAndState(id, DefaultConstant.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:ProfileService getById", e);
        }
    }

    @Override
    public Profile getByUserId(String userId) throws InternalException {
        try {
            return profileRepository.getByUserIdAndState(userId, DefaultConstant.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:ProfileService getByUserId", e);
        }
    }

    @Override
    public List<Profile> readIterable() throws InternalException {
        try {
            return profileRepository.findAllByState(DefaultConstant.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:ProfileService readIterable", e);
        }
    }

    @Override
    public Page<Profile> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DefaultConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
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

            return profileRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Profile> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Profile createProfile(Profile profile) throws InternalException {
        try {
            profile.setState(DefaultConstant.STATUS_ACTIVE);
            return profileRepository.save(profile);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:ProfileService createProfile", e);
        }
    }

    @Override
    public void removeProfileById(String id) throws InternalException {
        try {
            Profile profile = profileRepository.getByIdAndState(id, DefaultConstant.STATUS_ACTIVE);
            if (profile == null) {
                LOGGER.error("Profile not found with id");
                throw new InternalException(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Profile с идентификатором не найден");
            }
            profile.setState(DefaultConstant.STATUS_DELETED);

            profileRepository.save(profile);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:ProfileService removeProfileById", e);
        }
    }
}
