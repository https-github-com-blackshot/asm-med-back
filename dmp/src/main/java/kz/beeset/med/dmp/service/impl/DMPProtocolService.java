package kz.beeset.med.dmp.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.dmp.configs.MongoConfig;
import kz.beeset.med.dmp.constant.DMPNotificationConstants;
import kz.beeset.med.dmp.constant.DMPPatientConstants;
import kz.beeset.med.dmp.constant.DMPProtocolConstants;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.DMPProtocol;
import kz.beeset.med.dmp.repository.DMPDoctorRepository;
import kz.beeset.med.dmp.repository.DMPProtocolRepository;
import kz.beeset.med.dmp.service.IDMPProtocolService;
import kz.beeset.med.dmp.model.custom.DMPFileProtocol;
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
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class DMPProtocolService implements IDMPProtocolService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPProtocolService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPProtocolRepository dmpProtocolRepository;

    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public Page<DMPProtocol> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPProtocolConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPProtocolConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPProtocolConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPProtocolConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("dmpId")) {
                query.addCriteria(Criteria.where(DMPProtocolConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(DMPProtocolConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DMPProtocolConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPProtocolConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPProtocolConstants.STATE_FIELD_NAME).is(DMPProtocolConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return dmpProtocolRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPProtocol> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPProtocol> search(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPProtocolConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPProtocolConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPProtocolConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPProtocolConstants.SORT_DIRECTION_DESC))
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

            Criteria criteria = new Criteria();

            criteria = criteria.andOperator(
                    Criteria.where(DMPProtocolConstants.DMP_ID_FIELD_NAME).is(allRequestParams.get("dmpId")),
                    Criteria.where(DMPProtocolConstants.STATE_FIELD_NAME).is(DMPNotificationConstants.STATUS_ACTIVE),
                    new Criteria().orOperator(
                            Criteria.where(DMPProtocolConstants.CODE_FIELD_NAME).in(searchString),
                            Criteria.where(DMPProtocolConstants.NAME_FIELD_NAME).regex(searchString),
                            Criteria.where(DMPProtocolConstants.DESCRIPTION_FIELD_NAME).regex(searchString)
                    )
            );

            Query query = new Query(criteria);

            return dmpProtocolRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPProtocol> search(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPProtocol> readIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpProtocolRepository.getAllByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPProtocol> readIterableByDMPId(String dmpId)" +
                    "-", e);
        }
    }

    @Override
    public DMPProtocol get(String id) throws InternalException {
        try {
            return dmpProtocolRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPProtocol get(String id)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPProtocol> getAllByUserId(String userId) throws InternalException {
        try {
            List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByUserId(userId);
            List<String> dmpIds = dmpDoctorList.stream().map(DMPDoctor::getDmpId).collect(Collectors.toList());
            return dmpProtocolRepository.findAllByDmpIdInAndState(dmpIds, DMPProtocolConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "getAllByDmpIds(List<String> dmpIds)" +
                    "-", e);
        }
    }

    @Override
    public DMPProtocol create(DMPProtocol dmpProtocol) throws InternalException {
        try {
            dmpProtocol.setCreatedBy("");
            dmpProtocol.setCreatedDate(LocalDateTime.now());
            dmpProtocol.setState(DMPProtocolConstants.STATUS_ACTIVE);

            return dmpProtocolRepository.save(dmpProtocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPProtocol create(DMPProtocol dmpProtocol)" +
                    "-", e);
        }
    }

    @Override
    public DMPProtocol update(DMPProtocol dmpProtocol) throws InternalException {
        try {
            dmpProtocol.setLastModifiedBy("");
            dmpProtocol.setLastModifiedDate(LocalDateTime.now());
            dmpProtocol.setState(DMPProtocolConstants.STATUS_ACTIVE);

            return dmpProtocolRepository.save(dmpProtocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPProtocol update(DMPProtocol dmpProtocol)" +
                    "-", e);
        }
    }

    @Override
    public DMPProtocol delete(String id) throws InternalException {
        try {
            DMPProtocol dmpProtocol = dmpProtocolRepository.getById(id);

            dmpProtocol.setLastModifiedBy("");
            dmpProtocol.setLastModifiedDate(LocalDateTime.now());

            dmpProtocol.setState(DMPProtocolConstants.STATUS_DELETED);

            return dmpProtocolRepository.save(dmpProtocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPProtocol delete(String id)" +
                    "-", e);
        }
    }

    @Override
    public DMPProtocol uploadFile(List<MultipartFile> files, String dmpProtocolId) throws InternalException, IOException {
        DMPProtocol dmpProtocol = get(dmpProtocolId);

            Map<String, String> fileIds = new HashMap<>();

            if (dmpProtocol.getFileIds() != null) {
                if (!dmpProtocol.getFileIds().isEmpty()) {
                    fileIds.putAll(dmpProtocol.getFileIds());
                }
            }

            for (MultipartFile file : files) {
                String mimeType = file.getContentType();
                String fileName = file.getName() + " - " + dmpProtocol.getId();
                String fileId = saveDocument(file.getInputStream(), mimeType, fileName);
                LOGGER.debug("[uploadGuideDocument] - fileId: " + fileId);
                fileIds.put(fileId, file.getOriginalFilename());
            }

            dmpProtocol.setFileIds(fileIds);
            return dmpProtocol;

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
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:saveDocument(InputStream content, String mimeType, String fileName)", e);
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
