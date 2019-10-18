package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPDoctorCustom;
import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.repository.DMPDoctorRepository;
import kz.beeset.med.dmp.repository.DMPMDTTemplateRepository;
import kz.beeset.med.dmp.repository.DMPPatientRepository;
import kz.beeset.med.dmp.service.IDMPDoctorService;
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
public class DMPDoctorService implements IDMPDoctorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPDoctorService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;

    @Autowired
    private DMPMDTTemplateRepository dmpmdtTemplateRepository;

    @Autowired
    private DMPPatientRepository dmpPatientRepository;

    @Autowired
    private IAdminService adminService;

    @Override
    public Page<DMPDoctor> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPDoctorConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPDoctorConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPDoctorConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPDoctorConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPDoctorConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("userId")) {
                query.addCriteria(Criteria.where(DMPDoctorConstants.USER_ID_FIELD_NAME).is(allRequestParams.get("userId")));
            }
            if (allRequestParams.containsKey("codeNumber")) {
                query.addCriteria(Criteria.where(DMPDoctorConstants.CODE_NUMBER_FIELD_NAME).is(allRequestParams.get("codeNumber")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPDoctorConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPDoctorConstants.STATE_FIELD_NAME).is(DMPDoctorConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpDoctorRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDoctor> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPDoctor> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPDoctorConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPDoctorConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPDoctorConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPDoctorConstants.SORT_DIRECTION_DESC))
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

            List<String> userIds = new ArrayList<>();
            for(LinkedHashMap<String, Object> user: usersResponseContent){
                userIds.add((String) user.get("id"));
            }
            Criteria criteria = new Criteria();

            criteria = criteria.andOperator(
                    Criteria.where(DMPDoctorConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")),
                    Criteria.where(DMPDoctorConstants.STATE_FIELD_NAME).is(DMPNotificationConstants.STATUS_ACTIVE),
                    new Criteria().orOperator(
                            Criteria.where(DMPDoctorConstants.USER_ID_FIELD_NAME).in(userIds)
                    )
            );

            Query query = new Query(criteria);

            return dmpDoctorRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDoctor> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPDoctorCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            return getDMPDoctorCustomPageable(readPageable(allRequestParams));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDoctor> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPDoctorCustom> searchCustom(Map<String, String> allRequestParams) throws InternalException {
        try {
            return getDMPDoctorCustomPageable(search(allRequestParams));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDoctor> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    public Page<DMPDoctorCustom> getDMPDoctorCustomPageable(Page<DMPDoctor> dmpDoctorPage) throws InternalException{
        try {
            List<DMPDoctorCustom> dmpDoctorCustomList = new ArrayList<>();
            for(DMPDoctor dmpDoctor: dmpDoctorPage.getContent()){

                DMPDoctorCustom dmpDoctorCustom = new DMPDoctorCustom();
                dmpDoctorCustom.setDmpDoctor(dmpDoctor);



                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpDoctor.getUserId()).getBody();
                Gson gson = new Gson();
                JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                User user = gson.fromJson(data, User.class);
                dmpDoctorCustom.setUser(user);


                dmpDoctorCustomList.add(dmpDoctorCustom);

            }

            Page<DMPDoctorCustom> dmpDoctorCustomPageable = new PageImpl<>(dmpDoctorCustomList, dmpDoctorPage.getPageable(), dmpDoctorPage.getTotalElements());

            return dmpDoctorCustomPageable;
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPPatientCustom> getDMPPatientCustomPageable(Page<DMPPatient> dmpPatientPageable)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPDoctor> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpDoctorRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDoctor> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPDoctor> readIterableByIds(List<String> dmpDoctorIds) throws InternalException {
        try {
            return dmpDoctorRepository.findAllByIdInAndState(dmpDoctorIds, DMPDoctorConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDoctor> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }


    @Override
    public List<DMPDoctorCustom> readIterableByTemplateId(String dmpId,String templateId) throws InternalException {
        try {
            DMPMDTTemplate template = this.dmpmdtTemplateRepository.getById(templateId);
            List<String> doctorList = template.getDoctors();
            List<DMPDoctorCustom> dmpDoctorCustomList = new ArrayList<>();
            for (String doctorId: doctorList) {
                DMPDoctor dmpDoctor = dmpDoctorRepository.findByDmpIdAndUserIdAndState(dmpId, doctorId, DMPDoctorConstants.STATUS_ACTIVE);

                LinkedHashMap<String, Object> usersResponseBody = (LinkedHashMap<String, Object>) adminService.getUser(doctorId).getBody();
                LinkedHashMap<String, Object> usersResponseData = (LinkedHashMap<String, Object>) usersResponseBody.get("data");
                JsonObject userJsonObject = new Gson().toJsonTree(usersResponseData).getAsJsonObject();
                User user = new Gson().fromJson(userJsonObject, User.class);

                DMPDoctorCustom doctorCustom = new DMPDoctorCustom();
                doctorCustom.setDmpDoctor(dmpDoctor);
                doctorCustom.setUser(user);

                dmpDoctorCustomList.add(doctorCustom);
            }
            return dmpDoctorCustomList;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPDoctorCustom> readIterableByTemplateId(String dmpId,String templateId)" +
                    "-", e);
        }
    }

    @Override
    public DMPDoctor get(String id) throws InternalException {
        try {
            return dmpDoctorRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor get(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPDoctor> getAllByUserId(String id) throws InternalException {
        try {
            return dmpDoctorRepository.getAllByUserId(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPDoctor create(DMPDoctor dmpDoctor) throws InternalException {
        try {
            dmpDoctor.setCreatedBy("");
            dmpDoctor.setCreatedDate(LocalDateTime.now());
            dmpDoctor.setState(DMPDoctorConstants.STATUS_ACTIVE);

            return dmpDoctorRepository.save(dmpDoctor);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor create(DMPDoctor dmpDoctor)" +
                    "-", e);
        }
    }

    @Override
    public DMPDoctor update(DMPDoctor dmpDoctor) throws InternalException {
        try {
            dmpDoctor.setLastModifiedBy("");
            dmpDoctor.setLastModifiedDate(LocalDateTime.now());
            dmpDoctor.setState(DMPDoctorConstants.STATUS_ACTIVE);

            return dmpDoctorRepository.save(dmpDoctor);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor update(DMPDoctor dmpDoctor)" +
                    "-", e);
        }
    }

    @Override
    public DMPDoctor delete(String id) throws InternalException {
        try {
            DMPDoctor dmpDoctor = dmpDoctorRepository.getById(id);

            dmpDoctor.setLastModifiedBy("");
            dmpDoctor.setLastModifiedDate(LocalDateTime.now());

            dmpDoctor.setState(DMPDoctorConstants.STATUS_DELETED);

            return dmpDoctorRepository.save(dmpDoctor);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPDoctor delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<String> getDoctorUserIdByPatientUserIdAndDMPId(String userId, String dmpId) throws InternalException {
        try {
            DMPPatient dmpPatient = dmpPatientRepository.findByUserIdAndDmpIdAndState(userId, dmpId, DMPPatientConstants.STATUS_ACTIVE);
            List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.findByIdInAndDmpIdAndState(dmpPatient.getDmpDoctorIds(), dmpId, DMPDoctorConstants.STATUS_ACTIVE);
            List<String> dmpDoctorUserIds = dmpDoctorList.stream().map(DMPDoctor::getUserId).collect(Collectors.toList());
            return dmpDoctorUserIds;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in KiDoctorService getDoctorUserIdByPatientUserIdAndKiId(String userId, String kiId): {" +
                            "\nuserId: " + userId + "\n}", e);
        }
    }


}
