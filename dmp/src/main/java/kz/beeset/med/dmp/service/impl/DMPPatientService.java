package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.models.auth.In;
import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPDoctorCustom;
import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.repository.*;
import kz.beeset.med.dmp.service.IDMPPatientService;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class DMPPatientService implements IDMPPatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPPatientService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPPatientRepository dmpPatientRepository;
    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;
    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private DMPDeviceStatConfigRepository dmpDeviceStatConfigRepository;
    @Autowired
    private DMPDeviceStatRepository dmpDeviceStatRepository;

    @Override
    public Page<DMPPatientCustom> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPPatientConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPPatientConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPPatientConstants.HEALTH_STATUS_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPPatientConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPPatientConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("userId")) {
                query.addCriteria(Criteria.where(DMPPatientConstants.USER_ID_FIELD_NAME).is(allRequestParams.get("userId")));
            }
            if (allRequestParams.containsKey("codeNumber")) {
                query.addCriteria(Criteria.where(DMPPatientConstants.CODE_NUMBER_FIELD_NAME).is(allRequestParams.get("codeNumber")));
            }
            if (allRequestParams.containsKey("dmpDoctorId")) {
                List<String> doctorIds = new ArrayList<>();
                doctorIds.add(allRequestParams.get("dmpDoctorId"));
                query.addCriteria(Criteria.where(DMPPatientConstants.DMP_DOCTOR_IDS_FIELD_NAME).in(doctorIds));
            }

            if (allRequestParams.containsKey("healthStatus")) {
                query.addCriteria(Criteria.where(DMPPatientConstants.HEALTH_STATUS_FIELD_NAME).is(Integer.parseInt(allRequestParams.get("healthStatus"))));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPPatientConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPPatientConstants.STATE_FIELD_NAME).is(DMPPatientConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            Page<DMPPatient> dmpPatientPageable = dmpPatientRepository.findAll(query, pageableRequest);

            return getDMPPatientCustomPageable(dmpPatientPageable);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPPatient> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPPatientCustom> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPPatientConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPPatientConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPPatientConstants.HEALTH_STATUS_FIELD_NAME;

            String dmpDoctorUserId = null;
            if (allRequestParams.containsKey("dmpDoctorUserId")) {
                dmpDoctorUserId = allRequestParams.get("dmpDoctorUserId");
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPPatientConstants.SORT_DIRECTION_DESC))
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
            params.put("size", "50");

            LinkedHashMap<String, Object> usersResponseBody = (LinkedHashMap<String, Object>) adminService.searchUsers(params).getBody();
            LinkedHashMap<String, Object> usersResponseData = (LinkedHashMap<String, Object>) usersResponseBody.get("data");
            List<LinkedHashMap<String, Object>> usersResponseContent = (List<LinkedHashMap<String, Object>>) usersResponseData.get("content");

            List<String> userIds = new ArrayList<>();
            for (LinkedHashMap<String, Object> user : usersResponseContent) {
                userIds.add((String) user.get("id"));
            }
            Criteria criteria = new Criteria();

            criteria = criteria.andOperator(
                    Criteria.where(DMPPatientConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")),
                    Criteria.where(DMPPatientConstants.STATE_FIELD_NAME).is(DMPNotificationConstants.STATUS_ACTIVE),
                    new Criteria().orOperator(
                            dmpDoctorUserId != null ?
                                    Criteria.where(DMPPatientConstants.DMP_DOCTOR_IDS_FIELD_NAME).in(dmpDoctorUserId) :
                                    Criteria.where(DMPPatientConstants.USER_ID_FIELD_NAME).in(userIds),
                            Criteria.where(DMPPatientConstants.USER_ID_FIELD_NAME).in(userIds)
                    )
            );

            Query query = new Query(criteria);
            return getDMPPatientCustomPageable(dmpPatientRepository.findAll(query, pageableRequest));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPPatient> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    public Page<DMPPatientCustom> getDMPPatientCustomPageable(Page<DMPPatient> dmpPatientPageable) throws InternalException {
        try {
            List<DMPPatientCustom> dmpPatientCustomList = new ArrayList<>();
            for (DMPPatient dmpPatient : dmpPatientPageable.getContent()) {

                DMPPatientCustom dmpPatientCustom = new DMPPatientCustom();
                dmpPatientCustom.setDmpPatient(dmpPatient);


                DMP dmp = dmpRepository.getById(dmpPatient.getDmpId());
                dmpPatientCustom.setDmp(dmp);


                List<DMPDoctorCustom> dmpDoctorCustomList = new ArrayList<>();
                List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.findAllByIdInAndState(dmpPatient.getDmpDoctorIds(), DMPDoctorConstants.STATUS_ACTIVE);
                for (DMPDoctor dmpDoctor : dmpDoctorList) {
                    LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpDoctor.getUserId()).getBody();
                    Gson gson = new Gson();
                    JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                    User user = gson.fromJson(data, User.class);

                    DMPDoctorCustom dmpDoctorCustom = new DMPDoctorCustom();
                    dmpDoctorCustom.setDmpDoctor(dmpDoctor);
                    dmpDoctorCustom.setUser(user);
                    dmpDoctorCustomList.add(dmpDoctorCustom);
                }
                dmpPatientCustom.setDmpDoctorList(dmpDoctorCustomList);


                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpPatient.getUserId()).getBody();
                Gson gson = new Gson();
                JsonObject data = new JsonObject();

                try {
                    if (body != null && body.get("data") != null) {
                        data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                        User user = gson.fromJson(data, User.class);
                        dmpPatientCustom.setUser(user);
                    } else {
                        User newUser = new User();
                        newUser.setMiddlename(" ");
                        newUser.setName(" ");
                        newUser.setSurname("Не найдено");
                        dmpPatientCustom.setUser(newUser);
                    }
                } catch (NullPointerException nex) {
                    User newUser = new User();
                    newUser.setMiddlename(" ");
                    newUser.setName(" ");
                    newUser.setSurname("Не найдено");
                    dmpPatientCustom.setUser(newUser);
                    nex.printStackTrace();
                }

//                DMPDeviceStatConfig dmpDeviceStatConfig = dmpDeviceStatConfigRepository.getByDMPPatientId(dmpPatient.getId());
//                DMPDeviceStat dmpDeviceStat = dmpDeviceStatRepository.getByDMPPatientId(dmpPatient.getId());

                dmpPatientCustomList.add(dmpPatientCustom);

            }

            Page<DMPPatientCustom> dmpPatientCustomPageabele = new PageImpl<>(dmpPatientCustomList, dmpPatientPageable.getPageable(), dmpPatientPageable.getTotalElements());

            return dmpPatientCustomPageabele;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPPatientCustom> getDMPPatientCustomPageable(Page<DMPPatient> dmpPatientPageable)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPPatient> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpPatientRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPPatient> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPPatientCustom> readIterableByDMPIdAndDMPDoctorUserId(String dmpId, String dmpDoctorUserId) throws InternalException {
        try {
            List<DMPPatientCustom> valueablePatients = new ArrayList<>();
            List<DMPPatientCustom> lessValuablePatients = new ArrayList<>();
            List<DMPPatientCustom> notValuablePatients = new ArrayList<>();

            DMPDoctor dmpDoctor = dmpDoctorRepository.findByDmpIdAndUserIdAndState(dmpId, dmpDoctorUserId, DMPDoctorConstants.STATUS_ACTIVE);

            DMP dmp = dmpRepository.getById(dmpId);

            List<DMPPatient> dmpPatientList = dmpPatientRepository.findAllByDmpIdAndDmpDoctorIdsInAndState(dmpId, dmpDoctor.getId(), DMPPatientConstants.STATUS_ACTIVE);
            List<String> dmpPatientIdList = dmpPatientList.stream().map(DMPPatient::getId).collect(Collectors.toList());

            List<DMPDeviceStat> dmpDeviceStatList = dmpDeviceStatRepository.findAllByDmpPatientIdInAndState(dmpPatientIdList, DMPDeviceStatConstants.STATUS_ACTIVE);
            List<DMPDeviceStatConfig> dmpDeviceStatConfigList = dmpDeviceStatConfigRepository.findAllByDmpPatientIdInAndState(dmpPatientIdList, DMPDeviceStatConfigConstants.STATUS_ACTIVE);

            dmpPatientList.forEach(dmpPatient -> {
                List<DMPDeviceStat> dmpPatientDeviceStatList = dmpDeviceStatList.stream()
                        .filter(dmpDeviceStat -> dmpPatient.getId().equals(dmpDeviceStat.getDmpPatientId())).collect(Collectors.toList());
                List<DMPDeviceStatConfig> dmpPatientDeviceStatConfigList = dmpDeviceStatConfigList.stream()
                        .filter(dmpDeviceStatConfig -> dmpPatient.getId().equals(dmpDeviceStatConfig.getDmpPatientId())).collect(Collectors.toList());

                DMPPatientCustom dmpPatientCustom = new DMPPatientCustom();

                dmpPatientCustom.setDmp(dmp);
                dmpPatientCustom.setDmpPatient(dmpPatient);

                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpPatient.getUserId()).getBody();
                Gson gson = new Gson();
                JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                User user = gson.fromJson(data, User.class);
                dmpPatientCustom.setUser(user);

                AtomicBoolean everythingIsGood = new AtomicBoolean(true);
                AtomicBoolean somethingWrong = new AtomicBoolean(false);
                AtomicBoolean maybeWrongInTheFuture = new AtomicBoolean(false);

                if (dmpPatientDeviceStatList.size() > 0 && dmpPatientDeviceStatConfigList.size() > 0) {
                    DMPDeviceStat dmpDeviceStat = dmpPatientDeviceStatList.get(dmpPatientDeviceStatList.size() - 1);
                    DMPDeviceStatConfig dmpDeviceStatConfig = dmpPatientDeviceStatConfigList.get(0);
                    dmpDeviceStat.getParameterMap().keySet().forEach(key -> {
                        Object valueFromDevice = dmpDeviceStat.getParameterMap().get(key);
                        DMPDeviceStatConfigObjValue referenceValues = dmpDeviceStatConfig.getParameterMap().get(key);

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

                            }
                        }
                    });
                }

                if (everythingIsGood.get()) {
                    dmpPatientCustom.setHealthStatus(3);
                    notValuablePatients.add(dmpPatientCustom);
                } else if (!somethingWrong.get() && maybeWrongInTheFuture.get()) {
                    dmpPatientCustom.setHealthStatus(2);
                    lessValuablePatients.add(dmpPatientCustom);
                } else {
                    dmpPatientCustom.setHealthStatus(1);
                    valueablePatients.add(dmpPatientCustom);
                }

            });

            List<DMPPatientCustom> listToReturn = new ArrayList<>(valueablePatients);
            listToReturn.addAll(lessValuablePatients);
            listToReturn.addAll(notValuablePatients);

            return listToReturn;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPPatient> readIterableByDMPIdAndDMPDoctorUserId(String dmpId, String dmpDoctorUserId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPPatient> getAllByUserId(String id) throws InternalException {
        try {
            return dmpPatientRepository.getAllByUserId(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor get(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPPatient> getAllByDoctorUserId(String userId) throws InternalException {
        try {
            List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByUserId(userId);
            List<String> dmpDoctorIds = dmpDoctorList.stream().map(DMPDoctor::getId).collect(Collectors.toList());
            return dmpPatientRepository.findAllByDmpDoctorIdsInAndState(dmpDoctorIds, DMPProtocolConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPPatient> getAllByDoctorUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public DMPPatient get(String id) throws InternalException {
        try {
            return dmpPatientRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPPatient get(String id)" +
                    "-", e);
        }
    }


    @Override
    public DMPPatient create(DMPPatient dmpPatient) throws InternalException {
        try {
            dmpPatient.setCreatedBy("");
            dmpPatient.setCreatedDate(LocalDateTime.now());
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);

            return dmpPatientRepository.save(dmpPatient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPPatient create(DMPPatient dmpPatient)" +
                    "-", e);
        }
    }

    @Override
    public DMPPatient update(DMPPatient dmpPatient) throws InternalException {
        try {
            dmpPatient.setLastModifiedBy("");
            dmpPatient.setLastModifiedDate(LocalDateTime.now());
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);

            return dmpPatientRepository.save(dmpPatient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPPatient update(DMPPatient dmpPatient)" +
                    "-", e);
        }
    }

    @Override
    public DMPPatient delete(String id) throws InternalException {
        try {
            DMPPatient dmpPatient = dmpPatientRepository.getById(id);

            dmpPatient.setLastModifiedBy("");
            dmpPatient.setLastModifiedDate(LocalDateTime.now());

            dmpPatient.setState(DMPPatientConstants.STATUS_DELETED);

            return dmpPatientRepository.save(dmpPatient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPPatient delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPPatient createPatientTemp(String patientIdn, String doctorCodeNumber, DMPPatient dmpPatient) throws InternalException {
        try {
            DMPDoctor dmpDoctor = dmpDoctorRepository.findByDmpIdAndCodeNumberAndState(dmpPatient.getDmpId(), doctorCodeNumber, 1);
            List<String> dmpDoctorIds = new ArrayList<>();
            dmpDoctorIds.add(dmpDoctor.getId());

            Map<String, String> params = new HashMap<>();
            params.put("searchString", patientIdn);
            params.put("size", "1");

            LinkedHashMap<String, Object> usersResponseBody = (LinkedHashMap<String, Object>) adminService.searchUsers(params).getBody();
            LinkedHashMap<String, Object> usersResponseData = (LinkedHashMap<String, Object>) usersResponseBody.get("data");
            List<LinkedHashMap<String, Object>> usersResponseContent = (List<LinkedHashMap<String, Object>>) usersResponseData.get("content");

            LinkedHashMap<String, Object> user = usersResponseContent.get(0);
            String userId = (String) user.get("id");

            dmpPatient.setUserId(userId);
            dmpPatient.setDmpDoctorIds(dmpDoctorIds);
            dmpPatient.setHealthStatus(6); // Устройство не зарегистрировано

            dmpPatient.setCreatedBy("");
            dmpPatient.setCreatedDate(LocalDateTime.now());
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);

            return dmpPatientRepository.save(dmpPatient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPPatient delete(String id)" +
                    "-", e);
        }
    }
}
