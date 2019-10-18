package kz.beeset.med.dmp.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.configs.MongoConfig;
import kz.beeset.med.dmp.constant.DMPNotificationConstants;
import kz.beeset.med.dmp.constant.DMPPatientAppealConstants;
import kz.beeset.med.dmp.constant.DMPPatientConstants;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.common.CommentAndAttachmentInfo;
import kz.beeset.med.dmp.model.common.CommentAttachment;
import kz.beeset.med.dmp.model.custom.DMPPatientAppealCustom;
import kz.beeset.med.dmp.repository.*;
import kz.beeset.med.dmp.service.IDMPPatientAppealService;
import kz.beeset.med.dmp.service.feign.IAdminService;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import kz.beeset.med.dmp.utils.error.Pair;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class DMPPatientAppealService implements IDMPPatientAppealService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPPatientAppealService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPPatientAppealRepository dmpPatientAppealRepository;
    @Autowired
    private DMPPatientAppealCommentRepository dmpPatientAppealCommentRepository;
    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;
    @Autowired
    private GridFsOperations gridFsOperations;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private MongoConfig mongoConfig;
    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private IAdminService adminService;



    @Override
    public List<DMPPatientAppeal> getAllDMPPatientAppealList(Query query) throws InternalException {
        try {
            return dmpPatientAppealRepository.findAll(query);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPPatientAppealService getAllDMPPatientAppealList(Query query): {" +
                            "\nquery: " + query + "\n}", e);
        }
    }

    @Override
    public List<DMPPatientAppeal> getAllByStatusAndDMPDoctorId(String status, String dmpDoctorId) throws InternalException {
        try {

            return dmpPatientAppealRepository.findAllByDmpDoctorIdAndStatus(dmpDoctorId, status);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPPatientAppealService getAllByStatusAndDMPDoctorId : {" +
                            "\ndmpDoctorId: " + dmpDoctorId + "\n}", e);
        }
    }

    @Override
    public List<DMPPatientAppeal> getAllByStatus(String status) throws InternalException {
        try {

            return dmpPatientAppealRepository.findAllByStatus(status);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPPatientAppealService getAllByStatus : {" +
                            "\nstatus: " + status + "\n}", e);
        }
    }

    @Override
    public List<DMPPatientAppeal> getAllByStatusAndDMPDoctorAndDMP(String status, String dmpDoctorId, String dmpId) throws InternalException {
        try {

            return dmpPatientAppealRepository.findAllByDmpDoctorIdAndStatusAndDmpId(dmpDoctorId, status, dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in PatientAppealService getAllByStatusAndDMPDoctorAndDMP : {" +
                            "\ndmpDoctorId: " + dmpDoctorId + "\n}", e);
        }
    }

    @Override
    public Page<DMPPatientAppeal> getAllByDMPDoctor(String dmpDoctorId, String dmpId, Map<String, String> allRequestParams) throws InternalException {
        try {
            Sort.Direction sortDirection = Sort.Direction.DESC;

            int pageNumber = 0;

            int pageSize = 5;

            String sortBy = "id";

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

            Page<DMPPatientAppeal> patientAppealPage = dmpPatientAppealRepository.getAllByDmpDoctorIdAndDmpIdAndStatus(dmpDoctorId, dmpId, "active", pageableRequest);

            return patientAppealPage;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in PatientAppealService getAllByDoctor : { dmpDoctorId: "
                            + dmpDoctorId + ", dmpId: " + dmpId + "}", e);
        }
    }

    @Override
    public Page<DMPPatientAppealCustom> getAllCustomByDMPDoctor(String dmpDoctorId, String dmpId, Map<String, String> allRequestParams) throws InternalException {
        try {
            return generateCustomAppealObject(getAllByDMPDoctor(dmpDoctorId, dmpId, allRequestParams));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in PatientAppealService getAllByDoctor : { dmpDoctorId: "
                            + dmpDoctorId + ", dmpId: " + dmpId + "}", e);
        }
    }

    @Override
    public Page<DMPPatientAppealCustom> searchCustomByDMPDoctor(String dmpDoctorId, String dmpId, Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.DESC;

            int pageNumber = DMPPatientConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPPatientConstants.DEFAUT_PAGE_SIZE;

            String sortBy = "id";

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
            for(LinkedHashMap<String, Object> user: usersResponseContent){
                userIds.add((String) user.get("id"));
            }

            List<DMPPatient> dmpPatientList = dmpPatientRepository.findAllByUserIdInAndState(userIds, DefaultConstant.STATUS_ACTIVE);
            List<String> dmpPatientIds = dmpPatientList.stream().map(DMPPatient::getId).collect(Collectors.toList());

            Criteria criteria = new Criteria();

            criteria.andOperator(
                    Criteria.where("dmpId").is(dmpId),
                    Criteria.where("dmpDoctorId").is(dmpDoctorId),
                    Criteria.where("status").is("active"),
                    new Criteria().orOperator(
                            Criteria.where("dmpPatientId").in(dmpPatientIds)
                    )
            );

            Query query = new Query(criteria);

            return generateCustomAppealObject(dmpPatientAppealRepository.findAll(query, pageableRequest));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in PatientAppealService searchCustomByDMPDoctor : { dmpDoctorId: "
                            + dmpDoctorId + ", dmpId: " + dmpId + "}", e);
        }
    }

    public Page<DMPPatientAppealCustom> generateCustomAppealObject(Page<DMPPatientAppeal> patientAppealPage) {
        List<DMPPatientAppeal> patientAppealList = patientAppealPage.getContent();
        List<DMPPatientAppealCustom> response = new ArrayList<>();

        patientAppealList.forEach(patientAppeal -> {
            DMPPatientAppealCustom custom = new DMPPatientAppealCustom();
            custom.setDmpPatientAppeal(patientAppeal);

            DMPPatient dmpPatient = dmpPatientRepository.getById(patientAppeal.getDmpPatientId());
            LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpPatient.getUserId()).getBody();
            custom.setPatientUser(body.get("data"));
            response.add(custom);
        });

        return new PageImpl<>(response, patientAppealPage.getPageable(), patientAppealPage.getTotalElements());
    }

    @Override
    public List<DMPPatientAppeal> getAllByStatusAndDMPId(String status, String dmpId) throws InternalException {
        return null;
    }

    @Override
    public DMPPatientAppeal getById(String id) throws InternalException {
        try {

            return dmpPatientAppealRepository.findById(id).orElse(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при получении обращения пациента",
                    e, new Pair("id", id));
        }
    }

    @Override
    public void deleteById(String id) throws InternalException {

    }

    @Override
    public List<String> create(DMPPatientAppeal appeal) throws InternalException {
        try {
            if (appeal == null) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Нельзя создавать patientAppeal null");
            }

            List<String> dmpPatientAppealIds = new ArrayList<>();

            if (appeal.isMessageForAll()) {
                LOGGER.info("[DMPPatientAppealService] create() dmpId: " + appeal.getDmpId());
                List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByDMPId(appeal.getDmpId());
                for (DMPDoctor dmpDoctor : dmpDoctorList) {
                    LOGGER.info("[DMPPatientAppealService] create() dmpDoctorId: " + dmpDoctor.getId());
                    DMPPatientAppeal copyAppeal = new DMPPatientAppeal();
                    copyAppeal.setAttachments(appeal.getAttachments());
                    copyAppeal.setDmpDoctorId(dmpDoctor.getId());
                    copyAppeal.setEventType(appeal.getEventType());
                    copyAppeal.setMessageForAll(appeal.isMessageForAll());
                    copyAppeal.setStatus(appeal.getStatus());
                    copyAppeal.setProjectCode(appeal.getProjectCode());
                    copyAppeal.setNote(appeal.getNote());
                    copyAppeal.setTitle(appeal.getTitle());
                    copyAppeal.setDmpId(appeal.getDmpId());
                    copyAppeal.setDmpPatientId(appeal.getDmpPatientId());

                    copyAppeal.setEventDate(LocalDateTime.now().plusHours(6));

                    copyAppeal.setState(DefaultConstant.STATUS_ACTIVE);

                    copyAppeal = dmpPatientAppealRepository.save(copyAppeal);

                    dmpPatientAppealIds.add(copyAppeal.getId());
                }

            } else {

                appeal.setState(DefaultConstant.STATUS_ACTIVE);

                appeal.setEventDate(LocalDateTime.now().plusHours(6));

                appeal = dmpPatientAppealRepository.save(appeal);

                dmpPatientAppealIds.add(appeal.getId());
            }

            return dmpPatientAppealIds;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPPatientAppealService create : {" +
                            "\nappeal: " + appeal + "\n}", e);
        }
    }

    @Override
    public List<DMPPatientAppeal> createAppeals(List<DMPPatientAppeal> appealList) throws InternalException {
        try {
            if (appealList == null || appealList.isEmpty()) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при создании обращения пациента: пустой список");
            }

            return dmpPatientAppealRepository.saveAll(appealList);
        } catch (InternalException ie) {
            LOGGER.error(ie.getMessage(), ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при создании обращения пациента",
                    e, new Pair("appealList", appealList));
        }
    }

    @Override
    public DMPPatientAppeal update(DMPPatientAppeal appeal) throws InternalException {
        try {
//            appeal.setLastModifiedDate(LocalDateTime.now());
//            appeal.setLastModifiedBy("");

            if (appeal == null || appeal.getId() == null) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Нельзя обновлять patientAppeal null");
            }

            DMPPatientAppeal newAppeal = dmpPatientAppealRepository.findById(appeal.getId()).orElse(null);

            if (newAppeal == null)
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Обращение с таким идентификатором не найдено.");

            if (newAppeal.getDmpPatientId() != null && !newAppeal.getDmpPatientId().equals(appeal.getDmpPatientId()))
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Доступ запрещен");

            return dmpPatientAppealRepository.save(appeal);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                    "Exception in DMPPatientAppealService update: {" +
                            "\nappeal: " + appeal + "\n}", e);
        }
    }

    @Override
    public DMPPatientAppeal updateStatus(String appealId, String status) throws InternalException {
        try {
            DMPPatientAppeal patientAppeal = dmpPatientAppealRepository.findById(appealId).get();

            if (patientAppeal == null) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при изменении статуса обращения: обращение не найдено");
            }

            patientAppeal.setStatus(status);

            return dmpPatientAppealRepository.save(patientAppeal);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при изменении статуса" +
                    " обращении пациента status: " + status + " appealId: " + appealId);
        }
    }


    @Override
    public Object getStatusList() throws InternalException {
        try {
            return DMPPatientAppealConstants.getStatusList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, e.getMessage());
        }
    }

    @Override
    public Object getEventTypeList() throws InternalException {
        try {
            return DMPPatientAppealConstants.getEventTypeList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, e.getMessage());
        }
    }

    @Override
    public DMPPatientAppealComment createDMPatientAppealComment(DMPPatientAppealComment appealComment) throws InternalException {
        try {
            if (appealComment == null) {
                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при создании комментария на обращение пациента: пустой список");
            }

            appealComment.setId(null);
            appealComment.setState(DefaultConstant.STATUS_ACTIVE);

            return dmpPatientAppealCommentRepository.save(appealComment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при создании комментария на обращение пациента",
                    e, new Pair("appealComment", appealComment));
        }
    }

    @Override
    public List<DMPPatientAppealComment> getDMPPatientAppealCommentsByAppealId(String appealId) throws InternalException {
        try {
            return dmpPatientAppealCommentRepository.findAllByDmpPatientAppealIdAndState(appealId, DefaultConstant.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при получении списка комментариеев для обращении пациента",
                    e, new Pair("appealId", appealId));
        }
    }

//    @Override
//    public List<CommentAttachment> getDMPPatientAppealCommentAttachmentList(String commentId) throws InternalException {
//        try {
//            List<CommentAttachment> forReturn = new ArrayList<>();
//
//            DMPPatientAppealComment patientAppealComment = dmpPatientAppealCommentRepository.findById(commentId).get();
//            if (patientAppealComment == null) {
//                throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при поиске patientappealcomment by id: " + commentId);
//            }
//            List<String> attachments = patientAppealComment.getAttachments();
//            attachments.forEach(value -> {
//                GridFsResource resource = this.downloadDocument(value);
//
//                CommentAttachment tempAttachment = new CommentAttachment();
//                tempAttachment.setAttachmentId(value);
//                tempAttachment.setFileName(resource.getFilename());
//                tempAttachment.setMimeType(resource.getContentType());
//
//                forReturn.add(tempAttachment);
//
//                LOGGER.info("[getPatientAppealCommentAttachmentList()] mimeType contentType: " + resource.getContentType() + ", fileName: " + resource.getFilename());
//            });
//
//
//            return forReturn;
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при получении списка comment attachment для обращении пациента", e);
//        }
//    }

//    @Override
//    public List<CommentAndAttachmentInfo> getDMPPatientAppealCommentAttachmentInfo(String appealId) throws InternalException {
//        try {
//            List<CommentAndAttachmentInfo> forReturn = new ArrayList<>();
//            List<DMPPatientAppealComment> commentList = dmpPatientAppealCommentRepository.findAllByDmpPatientAppealIdAndState(appealId, DefaultConstant.STATUS_ACTIVE);
//            commentList.forEach(value -> {
//                try {
//                    CommentAndAttachmentInfo info = new CommentAndAttachmentInfo();
//
//                    info.setComment(value);
//                    info.setAttachmentList(this.getDMPPatientAppealCommentAttachmentList(value.getId()));
//
//                    forReturn.add(info);
//                } catch (InternalException e) {
//                    LOGGER.error("PatientAppealService [getPatientAppealCommentAttachmentInfo()] error using getPatientAppealCommentAttachmentList() " + e.getMessage(), e);
//                }
//            });
//            return forReturn;
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при получении списка комментариеев и информацией для обращении пациента",
//                    e, new Pair("appealId", appealId));
//        }
//    }


    @Override
    public List<DMP> getDMPListByUserId(String userId) throws InternalException {
        try {

            List<String> dmpIds = dmpDoctorRepository.getAllByUserId(userId).stream()
                    .map(DMPDoctor::getDmpId)
                    .collect(Collectors.toList());

            return dmpRepository.findAllByIdInAndState(dmpIds, DefaultConstant.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при получении списка organization",
                    e, new Pair("userId", userId));
        }
    }

    @Override
    public void uploadPatientAppealFiles(List<MultipartFile> files, List<String> dmpPatientAppealIds) throws InternalException {
        try {
            List<DMPPatientAppeal> dmpPatientAppeals = dmpPatientAppealRepository.findAllByIdIn(dmpPatientAppealIds);

            for (DMPPatientAppeal dmpPatientAppeal : dmpPatientAppeals) {

                Map<String, String> attachments = new HashMap<>();

                if (dmpPatientAppeal.getAttachments() != null && !dmpPatientAppeal.getAttachments().isEmpty()) {
                    attachments.putAll(dmpPatientAppeal.getAttachments());
                }

                for (MultipartFile file : files) {
                    String mimeType = file.getContentType();
                    String fileName = file.getName();
                    String fileId = saveDocument(file.getInputStream(), mimeType, fileName);

                    attachments.put(fileId, file.getOriginalFilename());
                }

                dmpPatientAppeal.setAttachments(attachments);

                dmpPatientAppeal.setState(DefaultConstant.STATUS_ACTIVE);

                dmpPatientAppealRepository.save(dmpPatientAppeal);

            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при загрузке документа",
                    e, new Pair("dmpPatientAppealIds", dmpPatientAppealIds));
        }
    }

    @Override
    public void uploadPatientAppealCommentFiles(List<MultipartFile> files, String dmpPatientAppealCommentId) throws InternalException {
        try {
            DMPPatientAppealComment dmpPatientAppealComment = dmpPatientAppealCommentRepository.findById(dmpPatientAppealCommentId).get();

            Map<String, String> attachments = new HashMap<>();

            if (dmpPatientAppealComment.getAttachments() != null && !dmpPatientAppealComment.getAttachments().isEmpty()) {
                attachments.putAll(dmpPatientAppealComment.getAttachments());
            }

            for (MultipartFile file : files) {
                String mimeType = file.getContentType();
                String fileName = file.getName();
                String fileId = saveDocument(file.getInputStream(), mimeType, fileName);

                attachments.put(fileId, file.getOriginalFilename());
            }

            dmpPatientAppealComment.setAttachments(attachments);

            dmpPatientAppealComment.setState(DefaultConstant.STATUS_ACTIVE);

            dmpPatientAppealCommentRepository.save(dmpPatientAppealComment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при загрузке документа",
                    e, new Pair("dmpPatientAppealComment", dmpPatientAppealCommentId));
        }
    }

    @Override
    public String saveDocument(InputStream content, String mimeType, String fileName) throws InternalException {
        try {
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "file");
            metaData.put("kind", "file");
            metaData.put("mimeType", mimeType);

            return gridFsOperations.store(content, fileName, mimeType, metaData).toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception: saveDocument(InputStream content, String mimeType, String fileName)", e);
        }
    }

    @Override
    public GridFsResource downloadDocument(String fileId) throws InternalException {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception: downloadDocument(String fileId)", e);
        }
    }

    private GridFSBucket getGridFs() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
    }
}
