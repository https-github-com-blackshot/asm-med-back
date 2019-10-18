package kz.beeset.med.dmp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPNotificationCustom;
import kz.beeset.med.dmp.model.custom.DMPRequestCustom;
import kz.beeset.med.dmp.service.IDMPService;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import kz.beeset.med.dmp.configs.MongoConfig;

import kz.beeset.med.dmp.repository.DMPDoctorRepository;
import kz.beeset.med.dmp.repository.DMPRepository;
import kz.beeset.med.dmp.repository.DMPRequestRepository;
import kz.beeset.med.dmp.service.IDMPRequestService;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class DMPRequestService implements IDMPRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPRequestService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPRequestRepository dmpRequestRepository;
    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;

    @Autowired
    private IDMPService dmpService;

    @Autowired
    private GridFsOperations gridFsOperations;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public Page<DMPRequest> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPRequestConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPRequestConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPRequestConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPRequestConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("fio")) {
                query.addCriteria(Criteria.where(DMPRequestConstants.FIO_FIELD_NAME).is(allRequestParams.get("fio")));
            }
            if (allRequestParams.containsKey("idn")) {
                query.addCriteria(Criteria.where(DMPRequestConstants.IDN_FIELD_NAME).is(allRequestParams.get("idn")));
            }
            if (allRequestParams.containsKey("phone")) {
                query.addCriteria(Criteria.where(DMPRequestConstants.PHONE_FIELD_NAME).is(allRequestParams.get("phone")));
            }
            if (allRequestParams.containsKey("email")) {
                query.addCriteria(Criteria.where(DMPRequestConstants.EMAIL_FIELD_NAME).is(allRequestParams.get("email")));
            }

            if (allRequestParams.containsKey("userId")) {
                query.addCriteria(Criteria.where(DMPRequestConstants.USER_ID_FIELD_NAME).is(allRequestParams.get("userId")));
            }

            if (allRequestParams.containsKey("state")) {
                LOGGER.info("STATE: " + Integer.parseInt(allRequestParams.get("state")));
                query.addCriteria(Criteria.where(DMPRequestConstants.STATE_FIELD_NAME).is(Integer.parseInt(allRequestParams.get("state"))));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPRequestConstants.SORT_DIRECTION_DESC))
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

            return dmpRequestRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPRequest> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPRequest> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPRequestConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPRequestConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPRequestConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPRequestConstants.SORT_DIRECTION_DESC))
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

            List<DMP> dmpList = dmpService.search(params).getContent();

            List<String> dmpIds = dmpList.stream().map(DMP::getId).collect(Collectors.toList());

            Criteria criteria = new Criteria();

            LOGGER.info("STATE: " + Integer.parseInt(allRequestParams.get("state")));

            criteria = criteria.andOperator(
                    Criteria.where(DMPRequestConstants.USER_ID_FIELD_NAME).is(allRequestParams.get("userId")),
                    allRequestParams.containsKey("state") ?
                            Criteria.where(DMPRequestConstants.STATE_FIELD_NAME).is(Integer.parseInt(allRequestParams.get("state"))) :
                            Criteria.where(DMPRequestConstants.USER_ID_FIELD_NAME).is(allRequestParams.get("userId")),
                    new Criteria().orOperator(
                            Criteria.where(DMPRequestConstants.DMP_IDS_FIELD_NAME).in(dmpIds),
                            Criteria.where(DMPRequestConstants.APPLY_DATE_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPRequestConstants.IDN_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPRequestConstants.FIO_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPRequestConstants.PHONE_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPRequestConstants.EMAIL_FIELD_NAME).regex(searchString),
                            allRequestParams.containsKey("state") ?
                                    Criteria.where(DMPRequestConstants.STATE_FIELD_NAME).is(Integer.parseInt(allRequestParams.get("state"))) :
                                    Criteria.where(DMPRequestConstants.USER_ID_FIELD_NAME).is(allRequestParams.get("userId"))
                    )
            );

            Query query = new Query(criteria);

            return dmpRequestRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPRequest> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPRequestCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try{
            return getDMPRequestCustomPageable(readPageable(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPRequestCustom> readCustomPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPRequestCustom> searchCustom(Map<String, String> allRequestParams) throws InternalException {
        try{
            return getDMPRequestCustomPageable(search(allRequestParams));
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPRequestCustom> searchCustom(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    private Page<DMPRequestCustom> getDMPRequestCustomPageable(Page<DMPRequest> dmpRequestPage) throws InternalException{
        try {
            List<DMPRequestCustom> dmpRequestCustomList = new ArrayList<>();

            List<DMPRequest> dmpRequestList = dmpRequestPage.getContent();
            for(DMPRequest dmpRequest : dmpRequestList){
                List<DMP> dmpList = dmpRepository.findAllByIdInAndState(dmpRequest.getDmpIds(), DMPConstants.STATUS_ACTIVE);

                DMPRequestCustom dmpRequestCustom = new DMPRequestCustom();
                dmpRequestCustom.setDmpRequest(dmpRequest);
                dmpRequestCustom.setDmpList(dmpList);

                dmpRequestCustomList.add(dmpRequestCustom);
            }

            return new PageImpl<>(dmpRequestCustomList, dmpRequestPage.getPageable(), dmpRequestPage.getTotalElements());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPRequestCustom> getDMPRequestCustomPageable(Page<DMPRequest> dmpRequestPage)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPRequest> readIterable() throws InternalException {
        try {
            return dmpRequestRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPRequest> readIterable(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPRequest get(String id) throws InternalException {
        try {
            return dmpRequestRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest get(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPRequest create(DMPRequest dmpRequest) throws InternalException {
        try {
            dmpRequest.setCreatedBy("");
            dmpRequest.setCreatedDate(LocalDateTime.now());
            dmpRequest.setApplyDate(LocalDate.now());
            dmpRequest.setState(DMPRequestConstants.STATUS_ACTIVE);

            return dmpRequestRepository.save(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest create(DMPRequest dmpRequest)" +
                    "-", e);
        }
    }

    @Override
    public DMPRequest update(DMPRequest dmpRequest) throws InternalException {
        try {
            dmpRequest.setLastModifiedBy("");
            dmpRequest.setLastModifiedDate(LocalDateTime.now());
            dmpRequest.setState(DMPRequestConstants.STATUS_ACTIVE);

            return dmpRequestRepository.save(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest update(DMPRequest dmpRequest)" +
                    "-", e);
        }
    }

    @Override
    public DMPRequest accept(DMPRequest dmpRequest) throws InternalException {
        try {
            dmpRequest.setLastModifiedBy("");
            dmpRequest.setLastModifiedDate(LocalDateTime.now());
            dmpRequest.setState(DMPRequestConstants.STATUS_ACCEPTED);

            List<DMP> dmpList = dmpRepository.findAllByIdInAndState(dmpRequest.getDmpIds(), DMPConstants.STATUS_ACTIVE);

            List<DMPDoctor> dmpDoctors = new ArrayList<>();
            for(DMP dmp: dmpList){
                DMPDoctor dmpDoctor = new DMPDoctor();
                dmpDoctor.setDmpId(dmp.getId());
                dmpDoctor.setCodeNumber(dmpRequest.getIdn());
                dmpDoctor.setUserId(dmpRequest.getUserId());

                dmpDoctor.setCreatedBy("");
                dmpDoctor.setCreatedDate(LocalDateTime.now());
                dmpDoctor.setState(DMPDoctorConstants.STATUS_ACTIVE);

                dmpDoctors.add(dmpDoctor);
            }

            dmpDoctorRepository.saveAll(dmpDoctors);

            return dmpRequestRepository.save(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest accept(DMPRequest dmpRequest)" + "-", e);
        }
    }

    @Override
    public DMPRequest decline(DMPRequest dmpRequest) throws InternalException {
        try {
            dmpRequest.setLastModifiedBy("");
            dmpRequest.setLastModifiedDate(LocalDateTime.now());
            dmpRequest.setState(DMPRequestConstants.STATUS_DECLINED);

            return dmpRequestRepository.save(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest decline(DMPRequest dmpRequest)" + "-", e);
        }
    }

    @Override
    public DMPRequest recall(String dmpRequestId) throws InternalException {
        try {
            DMPRequest dmpRequest = get(dmpRequestId);

            dmpRequest.setLastModifiedBy("");
            dmpRequest.setLastModifiedDate(LocalDateTime.now());
            dmpRequest.setState(DMPRequestConstants.STATUS_RECALLED);

            return dmpRequestRepository.save(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest decline(DMPRequest dmpRequest)" + "-", e);
        }
    }

    @Override
    public DMPRequest delete(String id) throws InternalException {
        try {
            DMPRequest dmpRequest = dmpRequestRepository.getById(id);

            dmpRequest.setLastModifiedBy("");
            dmpRequest.setLastModifiedDate(LocalDateTime.now());

            dmpRequest.setState(DMPRequestConstants.STATUS_DELETED);

            return dmpRequestRepository.save(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPRequest uploadFile(List<MultipartFile> files, String dmpRequestId) throws InternalException, IOException {
        try {
            DMPRequest dmpRequest = get(dmpRequestId);

            Map<String, String> fileIds = new HashMap<>();

            if (dmpRequest.getFileIds() != null && !dmpRequest.getFileIds().isEmpty()) {
                fileIds.putAll(dmpRequest.getFileIds());
            }

            for (MultipartFile file : files) {
                String mimeType = file.getContentType();
                String fileName = file.getName() + " - " + dmpRequest.getId();
                String fileId = saveDocument(file.getInputStream(), mimeType, fileName);

                fileIds.put(fileId, file.getOriginalFilename());
            }

            dmpRequest.setFileIds(fileIds);

            return update(dmpRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPRequest uploadFile(List<MultipartFile> files, String dmpRequestId)" +
                    "-", e);
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
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception: String saveDocument(InputStream content, String mimeType, String fileName)", e);
        }
    }

    @Override
    public GridFsResource downloadDocument(String fileId) throws InternalException {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:downloadDocument(String fileId)", e);
        }
    }

    private GridFSBucket getGridFs() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
    }
}
