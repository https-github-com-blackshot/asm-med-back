package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPNotification;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPNotificationCustom;
import kz.beeset.med.dmp.repository.DMPNotificationRepository;
import kz.beeset.med.dmp.repository.DMPPatientRepository;
import kz.beeset.med.dmp.service.IDMPNotificationService;
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
public class DMPNotificationService implements IDMPNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPNotificationService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPNotificationRepository dmpNotificationRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;
    @Autowired
    private IAdminService adminService;

    @Override
    public Page<DMPNotification> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPNotificationConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPNotificationConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPNotificationConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("type")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.TYPE_FIELD_NAME).is(allRequestParams.get("type")));
            }
            if (allRequestParams.containsKey("message")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.MESSAGE_FIELD_NAME).is(allRequestParams.get("message")));
            }
            if (allRequestParams.containsKey("beginDate")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.BEGIN_DATE_FIELD_NAME).is(allRequestParams.get("beginDate")));
            }
            if (allRequestParams.containsKey("endDate")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.END_DATE_FIELD_NAME).is(allRequestParams.get("endDate")));
            }
            if (allRequestParams.containsKey("period")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.PERIOD_FIELD_NAME).is(allRequestParams.get("period")));
            }
            if (allRequestParams.containsKey("eventTime")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.EVENT_TIME_FIELD_NAME).is(allRequestParams.get("eventTime")));
            }
            if (allRequestParams.containsKey("dmpDoctorId")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.DMP_DOCTOR_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorId")));
            }
            if (allRequestParams.containsKey("dmpDoctorUserId")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.DMP_DOCTOR_USER_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorUserId")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPNotificationConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPNotificationConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPNotificationConstants.STATE_FIELD_NAME).is(DMPNotificationConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpNotificationRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPNotification> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPNotification> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPNotificationConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPNotificationConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPNotificationConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPNotificationConstants.SORT_DIRECTION_DESC))
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

            Map<String, String> params = new HashMap<>();
            params.put("searchString", searchString);
            params.put("size", "1000");

            LinkedHashMap<String, Object> usersResponseBody = (LinkedHashMap<String, Object>) adminService.searchUsers(params).getBody();
            LinkedHashMap<String, Object> usersResponseData = (LinkedHashMap<String, Object>) usersResponseBody.get("data");
            List<LinkedHashMap<String, Object>> usersResponseContent = (List<LinkedHashMap<String, Object>>) usersResponseData.get("content");

            JsonObject jsonObject = (JsonObject) new Gson().toJsonTree(usersResponseBody);
//            LOGGER.info("============================= USERS RESPONSE BODY STARTED ==================================");
//            LOGGER.info(jsonObject);
//            System.out.println("============================= USERS RESPONSE BODY FINISHED ==================================");

            List<String> userIds = new ArrayList<>();
            for(LinkedHashMap<String, Object> user: usersResponseContent){
                userIds.add((String) user.get("id"));
            }

            List<DMPPatient> dmpPatientList = dmpPatientRepository.findAllByUserIdInAndState(userIds, DMPPatientConstants.STATUS_ACTIVE);
            List<String> dmpPatientIds = dmpPatientList.stream().map(DMPPatient::getId).collect(Collectors.toList());

            Criteria criteria = new Criteria();

            boolean toAll = false;
            if (searchString.equals("Все пациенты")){
                toAll = true;
            }

            criteria = criteria.andOperator(
                    Criteria.where(DMPNotificationConstants.DMP_DOCTOR_USER_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorUserId")),
                    Criteria.where(DMPNotificationConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")),
                    Criteria.where(DMPNotificationConstants.DMP_DOCTOR_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorId")),
                    Criteria.where(DMPNotificationConstants.STATE_FIELD_NAME).is(DMPNotificationConstants.STATUS_ACTIVE),
                    new Criteria().orOperator(
                            Criteria.where(DMPNotificationConstants.PATIENTS_FIELD_NAME).in(dmpPatientIds),
                            Criteria.where(DMPNotificationConstants.TYPE_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPNotificationConstants.BEGIN_DATE_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPNotificationConstants.END_DATE_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPNotificationConstants.EVENT_TIME_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPNotificationConstants.MESSAGE_FIELD_NAME).regex(searchString),
                            toAll ? Criteria.where(DMPNotificationConstants.TO_ALL_FIELD_NAME).is(true) :
                                    Criteria.where(DMPNotificationConstants.MESSAGE_FIELD_NAME).regex(searchString)
                    )
            );

            Query query = new Query(criteria);

            return dmpNotificationRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPNotification> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPNotificationCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
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
    public Page<DMPNotificationCustom> searchCustomPageable(Map<String, String> allRequestParams) throws InternalException {
        try{
            return getDMPNotificationCustomPageable(search(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPNotificationCustom> searchCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    public Page<DMPNotificationCustom> getDMPNotificationCustomPageable(Page<DMPNotification> dmpNotificationPage) throws InternalException{
        try {
            List<DMPNotificationCustom> dmpNotificationCustomList = new ArrayList<>();

            List<DMPNotification> dmpNotificationList = dmpNotificationPage.getContent();
            for(DMPNotification dmpNotification : dmpNotificationList){
                DMPNotificationCustom dmpNotificationCustom = new DMPNotificationCustom();

                dmpNotificationCustom.setDmpNotification(dmpNotification);

                if (!dmpNotification.getToAll()){
                    List<User> patients = new ArrayList<>();
                    dmpNotification.getPatients().forEach(dmpPatientId -> {

                        DMPPatient dmpPatient = dmpPatientRepository.getById(dmpPatientId);

                        LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpPatient.getUserId()).getBody();
                        Gson gson = new Gson();
                        JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                        User user = gson.fromJson(data, User.class);

                        patients.add(user);

                    });
                    dmpNotificationCustom.setPatients(patients);
                }


                dmpNotificationCustomList.add(dmpNotificationCustom);
            }

            return new PageImpl<>(dmpNotificationCustomList, dmpNotificationPage.getPageable(), dmpNotificationPage.getTotalElements());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPNotificationCustom> getDMPNotificationCustomPageable(Page<DMPNotification> dmpNotificationPage)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPNotification> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpNotificationRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPNotification> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPNotification get(String id) throws InternalException {
        try {
            return dmpNotificationRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPNotification get(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPNotification> getAllByUserId(String id) throws InternalException {
        try {
            return dmpNotificationRepository.getAllByUserId(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPNotification create(DMPNotification dmpNotification) throws InternalException {
        try {

            if (dmpNotification.getToAll()){
                List<DMPPatient> dmpPatientList = dmpPatientRepository.getAllByDMPId(dmpNotification.getDmpId());
                dmpNotification.setPatients(dmpPatientList.stream().map(DMPPatient::getId).collect(Collectors.toList()));
            }

            dmpNotification.setCreatedBy("");
            dmpNotification.setCreatedDate(LocalDateTime.now());
            dmpNotification.setState(DMPNotificationConstants.STATUS_ACTIVE);

            return dmpNotificationRepository.save(dmpNotification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPNotification create(DMPNotification dmpNotification)" +
                    "-", e);
        }
    }

    @Override
    public DMPNotification update(DMPNotification dmpNotification) throws InternalException {
        try {
            dmpNotification.setLastModifiedBy("");
            dmpNotification.setLastModifiedDate(LocalDateTime.now());
            dmpNotification.setState(DMPNotificationConstants.STATUS_ACTIVE);

            return dmpNotificationRepository.save(dmpNotification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPNotification update(DMPNotification dmpNotification)" +
                    "-", e);
        }
    }

    @Override
    public DMPNotification delete(String id) throws InternalException {
        try {
            DMPNotification dmpNotification = dmpNotificationRepository.getById(id);

            dmpNotification.setLastModifiedBy("");
            dmpNotification.setLastModifiedDate(LocalDateTime.now());

            dmpNotification.setState(DMPNotificationConstants.STATUS_DELETED);

            return dmpNotificationRepository.save(dmpNotification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPNotification delete(String id)" +
                    "-", e);
        }
    }
}
