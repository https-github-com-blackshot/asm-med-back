package kz.beeset.med.dmp.service.dmpv2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kz.beeset.med.dmp.constant.DMPNotificationConstants;
import kz.beeset.med.dmp.constant.DMPPatientConstants;
import kz.beeset.med.dmp.constant.DMPV2.DMPV2NotificationsConstants;
import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPNotificationCustom;
import kz.beeset.med.dmp.model.custom.DMPV2NotificationsCustom;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Notifications;
import kz.beeset.med.dmp.repository.dmpv2.DMPV2NotificationsRepository;
import kz.beeset.med.dmp.service.feign.IAdminService;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DMPV2NotificationsService implements IDMPV2NotificationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2NotificationsService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPV2NotificationsRepository notificationsRepository;
    @Autowired
    private IAdminService adminService;


    @Override
    public Page<DMPV2Notifications> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPV2NotificationsConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPV2NotificationsConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPV2NotificationsConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("type")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.TYPE_FIELD_NAME).is(allRequestParams.get("type")));
            }
            if (allRequestParams.containsKey("message")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.MESSAGE_FIELD_NAME).is(allRequestParams.get("message")));
            }
            if (allRequestParams.containsKey("beginDate")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.BEGIN_DATE_FIELD_NAME).is(allRequestParams.get("beginDate")));
            }
            if (allRequestParams.containsKey("endDate")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.END_DATE_FIELD_NAME).is(allRequestParams.get("endDate")));
            }
            if (allRequestParams.containsKey("period")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.PERIOD_FIELD_NAME).is(allRequestParams.get("period")));
            }
            if (allRequestParams.containsKey("eventTime")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.EVENT_TIME_FIELD_NAME).is(allRequestParams.get("eventTime")));
            }
            if (allRequestParams.containsKey("profileId")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.PROFILE_ID_FIELD_NAME).is(allRequestParams.get("profileId")));
            }
            if (allRequestParams.containsKey("doctorUserId")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.DOCTOR_USER_ID_FIELD_NAME).is(allRequestParams.get("doctorUserId")));
            }
            if (allRequestParams.containsKey("patientUserId")) {
                query.addCriteria(Criteria.where(DMPV2NotificationsConstants.PATIENT_USER_ID_FIELD_NAME).is(allRequestParams.get("patientUserId")));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPV2NotificationsConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPV2NotificationsConstants.STATE_FIELD_NAME).is(DMPV2NotificationsConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return notificationsRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2Notifications> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPV2Notifications> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPV2NotificationsConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPV2NotificationsConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPV2NotificationsConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPV2NotificationsConstants.SORT_DIRECTION_DESC))
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

            String searchString = allRequestParams.get("searchString");

            return notificationsRepository.query(searchString, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2Notifications> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPV2NotificationsCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try{
            return getDMPNotificationCustomPageable(readPageable(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPNotificationCustom> readCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPV2NotificationsCustom> searchCustomPageable(Map<String, String> allRequestParams) throws InternalException {
        try{
            return getDMPNotificationCustomPageable(search(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPNotificationCustom> searchCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    public Page<DMPV2NotificationsCustom> getDMPNotificationCustomPageable(Page<DMPV2Notifications> notificationsPage) throws InternalException{
        try {
            List<DMPV2NotificationsCustom> notificationsCustomList = new ArrayList<>();

            List<DMPV2Notifications> notificationList = notificationsPage.getContent();
            for(DMPV2Notifications notification : notificationList){
                DMPV2NotificationsCustom notificationsCustom = new DMPV2NotificationsCustom();

                notificationsCustom.setDmpv2Notification(notification);

                if (!notification.getToAll()){
                    List<User> patients = new ArrayList<>();
                    notification.getPatients().forEach(patientUserId -> {

                        LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(patientUserId).getBody();
                        Gson gson = new Gson();
                        JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                        User user = gson.fromJson(data, User.class);

                        patients.add(user);

                    });
                    notificationsCustom.setPatients(patients);
                }


                notificationsCustomList.add(notificationsCustom);
            }

            return new PageImpl<>(notificationsCustomList, notificationsPage.getPageable(), notificationsPage.getTotalElements());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2NotificationsCustom> getDMPNotificationCustomPageable(Page<DMPV2Notifications> notificationsPage)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2Notifications> readIterableByProfileId(String profileId) throws InternalException {
        try {
            return notificationsRepository.getAllByProfileId(profileId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2Notifications> readIterableByProfileId(String profileId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2Notifications> readIterableByDoctorUserIdAndPatientUserId(String doctorUserId, String patientUserId) throws InternalException {
        try {
            return notificationsRepository.getAllByDoctorUserIdAndPatientUserId(doctorUserId, patientUserId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2Notifications> readIterableByDoctorUserIdAndPatientUserId(String doctorUserId, String patientUserId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2Notifications> readIterableByPatientUserId(String patientUserId) throws InternalException {
        try {
            return notificationsRepository.getAllByPatientUserId(patientUserId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2Notifications> readIterableByPatientUserId(String patientUserId)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Notifications get(String id) throws InternalException {
        try {
            return notificationsRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2Notifications get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Notifications create(DMPV2Notifications notification) throws InternalException {
        try {

            notification.setState(DMPV2NotificationsConstants.STATUS_ACTIVE);

            return notificationsRepository.save(notification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2Notifications create(DMPV2Notifications notification)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Notifications update(DMPV2Notifications notification) throws InternalException {
        try {
            notification.setState(DMPV2NotificationsConstants.STATUS_ACTIVE);

            return notificationsRepository.save(notification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2Notifications update(DMPV2Notifications notification)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            DMPV2Notifications notification = notificationsRepository.getById(id);

            notification.setState(DMPV2NotificationsConstants.STATUS_DELETED);

            notificationsRepository.save(notification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }
    }
}
