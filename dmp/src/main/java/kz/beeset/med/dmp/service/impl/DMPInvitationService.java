package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPDoctorCustom;
import kz.beeset.med.dmp.model.custom.DMPInvitationCustom;
import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.repository.DMPInvitationRepository;
import kz.beeset.med.dmp.repository.DMPRepository;
import kz.beeset.med.dmp.service.IDMPInvitationService;
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

@Service
public class DMPInvitationService implements IDMPInvitationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPInvitationService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPInvitationRepository dmpInvitationRepository;
    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private IAdminService adminService;

    @Override
    public Page<DMPInvitation> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPInvitationConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPInvitationConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPInvitationConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("dmpDoctorId")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.DMP_DOCTOR_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorId")));
            }
            if (allRequestParams.containsKey("dmpDoctorUserId")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.DMP_DOCTOR_USER_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorUserId")));
            }
            if (allRequestParams.containsKey("patientUserId")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.PATIENT_USER_ID_FIELD_NAME).is(allRequestParams.get("patientUserId")));
            }
            if (allRequestParams.containsKey("message")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.MESSAGE_FIELD_NAME).is(allRequestParams.get("message")));
            }
            if (allRequestParams.containsKey("invitationStatus")) {
                query.addCriteria(Criteria.where(DMPInvitationConstants.INVITATION_STATUS_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("invitationStatus"))));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPInvitationConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPInvitationConstants.STATE_FIELD_NAME).is(DMPInvitationConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpInvitationRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPInvitation> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPInvitation> search(Map<String, String> allRequestParams) throws InternalException {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPInvitationConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPInvitationConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPInvitationConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPInvitationConstants.SORT_DIRECTION_DESC))
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
//            System.out.println("============================= USERS RESPONSE BODY STARTED ==================================");
//            System.out.println(jsonObject);
//            System.out.println("============================= USERS RESPONSE BODY FINISHED ==================================");

            List<String> userIds = new ArrayList<>();
            for(LinkedHashMap<String, Object> user: usersResponseContent){
                userIds.add((String) user.get("id"));
            }

            Criteria criteria = new Criteria();

            criteria = criteria.andOperator(
                    Criteria.where(DMPInvitationConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")),
                    Criteria.where(DMPInvitationConstants.DMP_DOCTOR_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorId")),
                    Criteria.where(DMPInvitationConstants.DMP_DOCTOR_USER_ID_FIELD_NAME).is(allRequestParams.get("dmpDoctorUserId")),
                    new Criteria().orOperator(
                            Criteria.where(DMPInvitationConstants.PATIENT_USER_ID_FIELD_NAME).in(userIds),
                            Criteria.where(DMPInvitationConstants.MESSAGE_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPInvitationConstants.INVITATION_STATUS_FIELD_NAME).regex(searchString)
                    )
            );

            Query query = new Query(criteria);

            return dmpInvitationRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPInvitation> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPInvitationCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try{
            return getDMPPatientCustomPageable(readPageable(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPInvitationCustom> readCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }

    }

    @Override
    public Page<DMPInvitationCustom> searchCustomPageable(Map<String, String> allRequestParams) throws InternalException {
        try{
            return getDMPPatientCustomPageable(search(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPInvitationCustom> readCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    public Page<DMPInvitationCustom> getDMPPatientCustomPageable(Page<DMPInvitation> dmpInvitationPage) throws InternalException{
        try {
            List<DMPInvitationCustom> dmpInvitationCustomList = new ArrayList<>();

            List<DMPInvitation> dmpInvitationList = dmpInvitationPage.getContent();
            for(DMPInvitation dmpInvitation : dmpInvitationList){
                DMPInvitationCustom dmpInvitationCustom = new DMPInvitationCustom();

                dmpInvitationCustom.setDmpInvitation(dmpInvitation);

                DMP dmp = dmpRepository.getById(dmpInvitation.getDmpId());
                dmpInvitationCustom.setDmp(dmp);

                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpInvitation.getPatientUserId()).getBody();
                Gson gson = new Gson();
                JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                User user = gson.fromJson(data, User.class);
                dmpInvitationCustom.setPatientUser(user);

                dmpInvitationCustomList.add(dmpInvitationCustom);
            }

            return new PageImpl<>(dmpInvitationCustomList, dmpInvitationPage.getPageable(), dmpInvitationPage.getTotalElements());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPInvitationCustom> getDMPPatientCustomPageable(Page<DMPInvitation> dmpInvitationPage)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPInvitation> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpInvitationRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPInvitation> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPInvitation get(String id) throws InternalException {
        try {
            return dmpInvitationRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPInvitation get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPInvitation create(DMPInvitation dmpInvitation) throws InternalException {
        try {
            dmpInvitation.setCreatedBy("");
            dmpInvitation.setCreatedDate(LocalDateTime.now());
            dmpInvitation.setState(DMPInvitationConstants.STATUS_ACTIVE);

            return dmpInvitationRepository.save(dmpInvitation);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPInvitation create(DMPInvitation dmpInvitation)" +
                    "-", e);
        }
    }

    @Override
    public DMPInvitation update(DMPInvitation dmpInvitation) throws InternalException {
        try {
            dmpInvitation.setLastModifiedBy("");
            dmpInvitation.setLastModifiedDate(LocalDateTime.now());
            dmpInvitation.setState(DMPInvitationConstants.STATUS_ACTIVE);

            return dmpInvitationRepository.save(dmpInvitation);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPInvitation update(DMPInvitation dmpInvitation)" +
                    "-", e);
        }
    }

    @Override
    public DMPInvitation delete(String id) throws InternalException {
        try {
            DMPInvitation dmpInvitation = dmpInvitationRepository.getById(id);

            dmpInvitation.setLastModifiedBy("");
            dmpInvitation.setLastModifiedDate(LocalDateTime.now());

            dmpInvitation.setState(DMPDeviceStatConfigConstants.STATUS_DELETED);

            return dmpInvitationRepository.save(dmpInvitation);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPInvitation delete(String id)" +
                    "-", e);
        }
    }
}
