package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.dmp.configs.MongoConfig;
import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.constant.DMPDeviceStatConfigConstants;
import kz.beeset.med.dmp.constant.DMPInvitationConstants;
import kz.beeset.med.dmp.constant.DMPMDTEventConstants;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.DMPMDTEventComment;
import kz.beeset.med.dmp.model.DMPMDTTemplate;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPMDTEventCustom;
import kz.beeset.med.dmp.repository.DMPMDTEventCommentRepository;
import kz.beeset.med.dmp.repository.DMPMDTEventRepository;
import kz.beeset.med.dmp.repository.DMPMDTTemplateRepository;
import kz.beeset.med.dmp.service.IDMPMDTEventService;
import kz.beeset.med.dmp.service.feign.IAdminService;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class DMPMDTEventService implements IDMPMDTEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPMDTEventService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPMDTEventRepository dmpmdtEventRepository;
    @Autowired
    private DMPMDTEventCommentRepository commentRepository;
    @Autowired
    private DMPMDTTemplateRepository templateRepository;

    @Autowired
    private GridFsOperations gridFsOperations;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public Page<DMPMDTEvent> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPMDTEventConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPMDTEventConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPMDTEventConstants.ID_FIELD_NAME;

            String userId = "";

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("teamId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("teamId")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("dmpVisitId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_VISIT_ID_FIELD_NAME).is(allRequestParams.get("dmpVisitId")));
            }
            if (allRequestParams.containsKey("dmpPatientId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_PATIENT_ID_FIELD_NAME).is(allRequestParams.get("dmpPatientId")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DESCRIPTION_DATE_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("description"))));
            }
            if (allRequestParams.containsKey("ownerId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.OWNER_ID_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("ownerId"))));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPMDTEventConstants.SORT_DIRECTION_DESC))
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
            if (allRequestParams.containsKey("userId")) {
                userId = allRequestParams.get("userId");
            }

            query.addCriteria(Criteria.where(DMPMDTEventConstants.STATE_FIELD_NAME).is(DMPMDTEventConstants.STATUS_ACTIVE));
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("pendingDoctorUserIdList").in(Collections.singletonList(userId)),
                    Criteria.where("acceptedDoctorUserIdList").in(Collections.singletonList(userId)),
                    Criteria.where("ownerId").is(userId)));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpmdtEventRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPMDTEvent> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPMDTEventCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPMDTEventConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPMDTEventConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPMDTEventConstants.ID_FIELD_NAME;

            String userId = "";

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("teamId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("teamId")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("dmpVisitId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_VISIT_ID_FIELD_NAME).is(allRequestParams.get("dmpVisitId")));
            }
            if (allRequestParams.containsKey("dmpPatientId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DMP_PATIENT_ID_FIELD_NAME).is(allRequestParams.get("dmpPatientId")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.DESCRIPTION_DATE_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("description"))));
            }
            if (allRequestParams.containsKey("ownerId")) {
                query.addCriteria(Criteria.where(DMPMDTEventConstants.OWNER_ID_FIELD_NAME).is(Boolean.parseBoolean(allRequestParams.get("ownerId"))));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPMDTEventConstants.SORT_DIRECTION_DESC))
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
            if (allRequestParams.containsKey("userId")) {
                userId = allRequestParams.get("userId");
            }

            query.addCriteria(Criteria.where(DMPMDTEventConstants.STATE_FIELD_NAME).is(DMPMDTEventConstants.STATUS_ACTIVE));
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("pendingDoctorUserIdList").in(Collections.singletonList(userId)),
                    Criteria.where("acceptedDoctorUserIdList").in(Collections.singletonList(userId)),
                    Criteria.where("ownerId").is(userId)));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));
            Page<DMPMDTEvent> pageableResponse = dmpmdtEventRepository.findAll(query, pageableRequest);

            return this.getDMPMDTEventCustomPageable(pageableResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPMDTEventCustom> readCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    public Page<DMPMDTEventCustom> getDMPMDTEventCustomPageable(Page<DMPMDTEvent> eventPage) throws InternalException{
        try {
            List<DMPMDTEventCustom> eventCustoms = new ArrayList<>();
            for(DMPMDTEvent event: eventPage.getContent()){

                DMPMDTEventCustom eventCustom = new DMPMDTEventCustom();
                eventCustom.setEvent(event);

                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(event.getOwnerId()).getBody();
                Gson gson = new Gson();
                JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                User user = gson.fromJson(data, User.class);
                eventCustom.setUser(user);

                eventCustoms.add(eventCustom);

            }

            Page<DMPMDTEventCustom> eventCustomPage = new PageImpl<>(eventCustoms, eventPage.getPageable(), eventPage.getTotalElements());

            return eventCustomPage;
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPMDTEventCustom> getDMPMDTEventCustomPageable(Page<DMPMDTEvent> eventPage)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPMDTEvent> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPMDTEventConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPMDTEventConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPMDTEventConstants.ID_FIELD_NAME;

            String userId = "";
            List<String> templateIdList = new ArrayList<>();

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPMDTEventConstants.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;

            }
            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("userId")) {
                userId = allRequestParams.get("userId");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }
            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpmdtEventRepository.query(allRequestParams.get("searchString"), userId, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPMDTEvent> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPMDTEvent> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpmdtEventRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPMDTEvent> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTEvent get(String id) throws InternalException {
        try {
            return dmpmdtEventRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent get(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPMDTEvent> getByUserId(String id) throws InternalException {
        try {
            return dmpmdtEventRepository.getByUserId(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent List<DMPMDTEvent> getByUserID" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTEvent create(DMPMDTEvent dmpmdtEvent) throws InternalException {
        try {
            dmpmdtEvent.setCreatedBy("");
            dmpmdtEvent.setCreatedDate(LocalDateTime.now());
            dmpmdtEvent.setState(DMPMDTEventConstants.STATUS_ACTIVE);

            return dmpmdtEventRepository.save(dmpmdtEvent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent create(DMPMDTEvent dmpmdtEvent)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTEvent update(DMPMDTEvent dmpmdtEvent) throws InternalException {
        try {
            dmpmdtEvent.setLastModifiedBy("");
            dmpmdtEvent.setLastModifiedDate(LocalDateTime.now());
            dmpmdtEvent.setState(DMPMDTEventConstants.STATUS_ACTIVE);

            return dmpmdtEventRepository.save(dmpmdtEvent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent update(DMPMDTEvent dmpmdtEvent)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTEvent delete(String id) throws InternalException {
        try {
            DMPMDTEvent dmpmdtEvent = dmpmdtEventRepository.getById(id);

            dmpmdtEvent.setLastModifiedBy("");
            dmpmdtEvent.setLastModifiedDate(LocalDateTime.now());

            dmpmdtEvent.setState(DMPMDTEventConstants.STATUS_DELETED);

            return dmpmdtEventRepository.save(dmpmdtEvent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPMDTEvent updateEventAddingCommentId(String eventId, String uploadCode, String commentId) throws InternalException {
        try {
            DMPMDTEvent eventDB = this.dmpmdtEventRepository.getById(eventId);

            for (int i = 0; i < eventDB.getEventUploadList().size(); i++) {
                if (eventDB.getEventUploadList().get(i).getCode().equalsIgnoreCase(uploadCode)) {
                    if (eventDB.getEventUploadList().get(i).getComments() == null) {
                        eventDB.getEventUploadList().get(i).setComments(new ArrayList<>());
                    }
                    eventDB.getEventUploadList().get(i).getComments().add(commentId);
                    break;
                }
            }

            return this.dmpmdtEventRepository.save(eventDB);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String updateEventAddingCommentId(doc Id) - ", e);
        }
    }

    @Override
    public DMPMDTEvent updateEventAcceptByDoctor(String eventId, String userId) throws InternalException {
        try {
            DMPMDTEvent event = this.dmpmdtEventRepository.getById(eventId);

            List<String> pending = event.getPendingDoctorUserIdList();
            pending.remove(userId);
            event.setPendingDoctorUserIdList(pending);

            List<String> accepted = event.getAcceptedDoctorUserIdList();
            if (accepted == null) {
                accepted = new ArrayList<>();
            }
            accepted.add(userId);
            event.setAcceptedDoctorUserIdList(accepted);

            return this.dmpmdtEventRepository.save(event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent updateEventAcceptByDoctor(String eventId, String userId) - ", e);
        }
    }

    @Override
    public DMPMDTEvent updateEventDeclineByDoctor(String eventId, String userId) throws InternalException {
        try {
            DMPMDTEvent event = this.dmpmdtEventRepository.getById(eventId);

            List<String> pending = event.getPendingDoctorUserIdList();
            pending.remove(userId);
            event.setPendingDoctorUserIdList(pending);

            return this.dmpmdtEventRepository.save(event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent updateEventDeclineByDoctor(String eventId, String userId) - ", e);
        }
    }

    @Override
    public void deleteFiles(List<String> idList) throws InternalException {
        try {
            for (String id : idList) {
                this.deleteDocById(id);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public GridFsResource downloadFile(String fileId) throws InternalException {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPMDTEvent downloadFile(String id)" +
                    "-", e);
        }
    }

    @Override
    public String saveFile(InputStream content, String mimeType, String fileName) throws IOException, InternalException {

        try {
            // Добавляем свою произвольную информацию к файлу
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("kind", "avatar");
            metaData.put("mimeType", mimeType);
            LOGGER.debug("[saveFile()] - mimeType: " + mimeType);
            return gridFsOperations.store(content, fileName, mimeType, metaData).toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String saveAvatar(MultipartFile file)" +
                    "-", e);
        }
    }

    @Override
    public void deleteDocById(String id) throws InternalException {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
            gridFsOperations.delete(query);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String deleteDocById(doc Id) - ", e);
        }
    }

    @Override
    public DMPMDTEventComment createComment(DMPMDTEventComment dmpmdtEventComment) throws InternalException {
        try {
            dmpmdtEventComment.setCreatedBy("");
            dmpmdtEventComment.setCreatedDate(LocalDateTime.now());
            return commentRepository.save(dmpmdtEventComment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String createComment(DMPMDTEventComment dmpmdtEventComment) - ", e);
        }
    }

    @Override
    public List<DMPMDTEventComment> getCommentListByEventId(String eventId) throws InternalException {
        try {
            Gson gson = new Gson();
            DMPMDTEvent event = dmpmdtEventRepository.getById(eventId);
            LOGGER.info("---INFO event uploadList:" + gson.toJsonTree(event.getEventUploadList()));
            List<DMPMDTEventComment> eventCommentList = new ArrayList<>();

            event.getEventUploadList().forEach(upload -> {
                try {
                    eventCommentList.addAll(this.commentRepository.getAllByIdIn(upload.getComments()));
                } catch (InternalException e) {
                    LOGGER.error(e.getMessage(), e);
                    LOGGER.error("--------------------CASE 1: String getCommentListByEventId(String eventId)--------------------");
                }
            });
            return eventCommentList;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "CASE 2: String getCommentListByEventId(String eventId) - ", e);
        }
    }

    private GridFSBucket getGridFs() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
    }
}
