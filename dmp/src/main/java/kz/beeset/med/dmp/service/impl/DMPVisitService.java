package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.constructor.model.data.Warehouse;
import kz.beeset.med.constructor.model.guide.Form;
import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPDeviceStatConfigConstants;
import kz.beeset.med.dmp.constant.DMPVisitConstants;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPVisit;
import kz.beeset.med.dmp.model.custom.CustomForm;
import kz.beeset.med.dmp.repository.DMPVisitRepository;
import kz.beeset.med.dmp.repository.data.WarehouseRepository;
import kz.beeset.med.dmp.repository.guide.FormsRepository;
import kz.beeset.med.dmp.service.IDMPVisitService;
import kz.beeset.med.dmp.service.feign.IFormService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DMPVisitService implements IDMPVisitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPVisitService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPVisitRepository dmpVisitRepository;
    @Autowired
    private IFormService formService;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private FormsRepository formsRepository;

    @Override
    public Page<DMPVisit> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPVisitConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPVisitConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPVisitConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("nameKz")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.NAMEKZ_FIELD_NAME).is(allRequestParams.get("nameKz")));
            }
            if (allRequestParams.containsKey("nameRu")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.NAMERU_FIELD_NAME).is(allRequestParams.get("nameRu")));
            }
            if (allRequestParams.containsKey("nameEn")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.NAMEEN_FIELD_NAME).is(allRequestParams.get("nameEn")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("dmpScheduleTypeId")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.DMP_SCHEDULE_TYPE_ID_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("dmpScheduleTypeId"))));
            }
            if (allRequestParams.containsKey("days")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.DAYS_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("days"))));
            }
            if (allRequestParams.containsKey("deadlineOffset")) {
                query.addCriteria(Criteria.where(DMPVisitConstants.DEADLINE_OFFSET_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("deadlineOffset"))));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPVisitConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPVisitConstants.STATE_FIELD_NAME).is(DMPVisitConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpVisitRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPVisit> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPVisit> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPVisitConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPVisitConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPVisitConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPVisitConstants.SORT_DIRECTION_DESC))
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

            return dmpVisitRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPVisit> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPVisit> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpVisitRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPVisit> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPVisit> readIterableByDMPScheduleTypeId(String dmpScheduleTypeId) throws InternalException {
        try {
            return dmpVisitRepository.getAllByDMPScheduleTypeId(dmpScheduleTypeId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPVisit> readIterableByDMPScheduleTypeId(String dmpScheduleTypeId)" +
                    "-", e);
        }
    }

    @Override
    public List<Form> readFormsByVisitId(String visitId) throws InternalException {
        try {
            DMPVisit visit = dmpVisitRepository.getById(visitId);
            List<Form> forms = new ArrayList<>();
            for (String formId : visit.getForms()) {

                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) formService.get(formId).getBody();
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) body.get("data");
                JsonObject formJsonObj = new Gson().toJsonTree(data).getAsJsonObject();
                Form f = new Gson().fromJson(formJsonObj, Form.class);

                forms.add(f);
            }
            forms.sort(Comparator.comparing(p -> p.getNameRu()));
            return forms;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in List<Form> readFormsByVisitId(String visitId): {" +
                            "\nvisitId: " + visitId + "\n}", e);
        }
    }

    @Override
    public List<CustomForm> getFormsSingleTypeVisitWHStatus(String visitId, String userId) throws InternalException {
        try {
            DMPVisit visit = dmpVisitRepository.getById(visitId);
            List<CustomForm> forms = new ArrayList<>();
            for (String formId : visit.getForms()) {

                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) formService.get(formId).getBody();
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) body.get("data");
                JsonObject formJsonObj = new Gson().toJsonTree(data).getAsJsonObject();
                Form f = new Gson().fromJson(formJsonObj, Form.class);

                boolean hasWarehouse = false;
                Map<String, Boolean> fulfilledMap = new HashMap<>();
                List<Warehouse> warehouseList = warehouseRepository.findAllByUserIdAndVisitIdAndFormId(userId, visitId, formId);
                if (warehouseList.size() > 0) {
                    hasWarehouse = true;
                }
                warehouseList.forEach(warehouse -> {
                    fulfilledMap.put(formId, warehouse.isCheckFullFill());
                });
                forms.add(new CustomForm(f, hasWarehouse, fulfilledMap));
            }
            forms.sort(Comparator.comparing(p -> p.getNameRu()));
            return forms;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in List<CustomForm> getFormsSingleTypeVisitWHStatus(String visitId, String userId): {" +
                            "\nvisitId: " + visitId + "\n}", e);
        }
    }

    @Override
    public List<Warehouse> getWarehouseListByVisitIdAndUserId(String visitId, String userId) throws InternalException {
        try {
            return warehouseRepository.findAllByVisitIdAndUserId(visitId, userId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Warehouse> getWarehouseListByVisitIdAndUserId(String visitId, String userId)" +
                    "-", e);
        }
    }

    @Override
    public Warehouse getWareHouseByVisitIdAndUserIdAndFormId(String visitId, String userId, String formId) throws InternalException {
        try {

            Warehouse warehouse = warehouseRepository.findWarehouseByVisitIdAndUserIdAndFormId(visitId, userId, formId);

            if (warehouse != null)
                return warehouse;
            else {

                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) formService.get(formId).getBody();
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) body.get("data");
                JsonObject dataJsonObject = new Gson().toJsonTree(data).getAsJsonObject();

                Form form = new Gson().fromJson(dataJsonObject, Form.class);


                Warehouse newWarehouse = new Warehouse();
                newWarehouse.setFormId(form.getId());
                newWarehouse.setFormCode(form.getCode());
                newWarehouse.setUserId(userId);
                newWarehouse.setVisitId(visitId);

                //Auditing
                newWarehouse.setState(DefaultConstant.STATUS_ACTIVE);

                return warehouseRepository.save(newWarehouse);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPVisitService getWareHouseByVisitIdAndUserIdAndFormId(String visitId, String userId, String fromId): {" +
                            "\nvisitId: " + visitId + "\n}", e);
        }
    }

    @Override
    public List<Form> getAllFormsByDMPIdAndScheduleTypeId(String dmpId, String scheduleTypeId) throws InternalException {
        try {
            List<DMPVisit> visitList = dmpVisitRepository.findAllByDmpIdAndDmpScheduleTypeIdAndState(dmpId, scheduleTypeId, DMPVisitConstants.STATUS_ACTIVE);
            List<String> formIds = new ArrayList<>();
            visitList.forEach((DMPVisit v) -> {
                v.getForms().forEach((String formId) -> {
                    if (!formIds.contains(formId))
                        formIds.add(formId);
                });
            });
            return formsRepository.findAllByIdInAndState(formIds, 1);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPVisitsService getAllFormsByScheduleTypeId(String kiId, String scheduleTypeId): ", e);
        }
    }

    @Override
    public DMPVisit get(String id) throws InternalException {
        try {
            return dmpVisitRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPVisit get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPVisit create(DMPVisit dmpVisit) throws InternalException {
        try {
            dmpVisit.setCreatedBy("");
            dmpVisit.setCreatedDate(LocalDateTime.now());
            dmpVisit.setState(DMPVisitConstants.STATUS_ACTIVE);

            return dmpVisitRepository.save(dmpVisit);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPVisit create(DMPVisit dmpVisit)" +
                    "-", e);
        }
    }

    @Override
    public DMPVisit update(DMPVisit dmpVisit) throws InternalException {
        try {
            dmpVisit.setLastModifiedBy("");
            dmpVisit.setLastModifiedDate(LocalDateTime.now());
            dmpVisit.setState(DMPVisitConstants.STATUS_ACTIVE);

            return dmpVisitRepository.save(dmpVisit);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPVisit update(DMPVisit dmpVisit)" +
                    "-", e);
        }
    }

    @Override
    public DMPVisit delete(String id) throws InternalException {
        try {
            DMPVisit dmpVisit = dmpVisitRepository.getById(id);

            dmpVisit.setLastModifiedBy("");
            dmpVisit.setLastModifiedDate(LocalDateTime.now());

            dmpVisit.setState(DMPVisitConstants.STATUS_DELETED);

            return dmpVisitRepository.save(dmpVisit);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPVisit delete(String id)" +
                    "-", e);
        }
    }


    @Override
    public DMPVisit getDmpVisitById(String id) throws InternalException {
        try {
            return dmpVisitRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPVisitService getDmpVisitById(String id): {" +
                            "\nid: " + id + "\n}", e);
        }
    }

    @Override
    public DMPVisit getByCodeAndDMPId(String code, String dmpId) throws InternalException {
        try {
            return dmpVisitRepository.getByDmpIdAndCode(dmpId, code);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in getByCodeAndDMPId(String code, String dmpId)):", e);
        }
    }

    @Override
    public String duplicateDmpVisit(String visitId, String code) throws InternalException {
        try {
            DMPVisit sourceVisit = this.getDmpVisitById(visitId);

            if (dmpVisitRepository.getByDmpIdAndCode(sourceVisit.getDmpId(), code) != null) {
                return "Визит с таким кодом существует! Попробуте другой!";
            }

            DMPVisit targetVisit = new DMPVisit();
            targetVisit.setDmpId(sourceVisit.getDmpId());
            targetVisit.setNameKz(sourceVisit.getNameKz() + " кошiрме");
            targetVisit.setNameRu(sourceVisit.getNameRu() + " копия");
            targetVisit.setNameEn(sourceVisit.getNameEn() + " copy");
            targetVisit.setDescription(sourceVisit.getDescription() + " копия");
            targetVisit.setDmpScheduleTypeId(sourceVisit.getDmpScheduleTypeId());
            targetVisit.setDays(sourceVisit.getDays());
            targetVisit.setCode(sourceVisit.getCode());
            targetVisit.setDeadlineOffset(sourceVisit.getDeadlineOffset());

            //TODO required checking for ids if not exists
            targetVisit.setForms(sourceVisit.getForms());

            //Auditing
            targetVisit.setState(DMPConstants.STATUS_ACTIVE);

            targetVisit.setCreatedBy("");
            targetVisit.setCreatedDate(LocalDateTime.now());
            targetVisit.setLastModifiedDate(LocalDateTime.now());
            targetVisit.setLastModifiedBy("");

            dmpVisitRepository.save(targetVisit);
            return "Визит успешно сдублирован!";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPVisitService duplicateDmpVisit(Visit visit): {" +
                            "\nvisit: " + visitId + "\n}", e);
        }

    }
}
