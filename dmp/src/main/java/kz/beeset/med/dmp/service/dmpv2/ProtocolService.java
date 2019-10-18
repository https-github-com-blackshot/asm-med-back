package kz.beeset.med.dmp.service.dmpv2;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.dmp.configs.MongoConfig;
import kz.beeset.med.dmp.constant.DMPConstants;
import kz.beeset.med.dmp.model.dmpv2.DMPV2;
import kz.beeset.med.dmp.model.dmpv2.Protocol;
import kz.beeset.med.dmp.repository.dmpv2.DMPV2Repository;
import kz.beeset.med.dmp.repository.dmpv2.ProtocolRepository;
import kz.beeset.med.dmp.utils.error.CloudocObjectNotFoundException;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ProtocolService implements IProtocolService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private ProtocolRepository repository;
    @Autowired
    private DMPV2Repository dmpv2Repository;
    @Autowired
    private GridFsOperations gridFsOperations;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public Page<Protocol> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DMPConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(DMPConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(DMPConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DMPConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("diseaseId")) {
                query.addCriteria(Criteria.where(DMPConstants.DISEASE_ID_FIELD_NAME).is(allRequestParams.get("diseaseId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DMPConstants.STATE_FIELD_NAME).is(DMPConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return repository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    " Page<Protocol> readPageable(Map<String, String> allRequestParams) )" +
                    "-", e);
        }
    }

    @Override
    public Page<Protocol> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DMPConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DMPConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DMPConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DMPConstants.SORT_DIRECTION_DESC))
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

            return repository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Protocol> searchPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }

    }

    @Override
    public List<Protocol> readIterable() throws InternalException {
        try {
            return repository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Protocol> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<Protocol> readIterableByDMPV2Id(String dmpV2Id) throws InternalException {
        try {
            DMPV2 dmpV2 = dmpv2Repository.getById(dmpV2Id);

            if (dmpV2 == null) {
                throw new CloudocObjectNotFoundException("DMPV2 с идентификатором '"
                        + dmpV2Id + "' не существует", dmpV2.getClass());
            }
            return repository.findAllByDiseaseIdInAndStateIs(dmpV2.getSelectedDiseaseIds(), DMPConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Protocol> readIterableByDMPV2Id(String dmpV2Id)" +
                    "-", e);
        }
    }

    @Override
    public Protocol readOne(String id) throws InternalException {
        try {
            return repository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Protocol readOne(String id)" +
                    "-", e);
        }
    }

    @Override
    public Protocol create(Protocol protocol) throws InternalException {
        try {
            protocol.setState(DMPConstants.STATUS_ACTIVE);
            return repository.save(protocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Protocol create(Protocol protocol)" +
                    "-", e);
        }
    }

    @Override
    public Protocol update(Protocol protocol) throws InternalException {
        try {
            protocol.setState(DMPConstants.STATUS_ACTIVE);
            return repository.save(protocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Protocol update(Protocol protocol)" +
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
                    "String saveFile(MultipartFile file)" +
                    "-", e);
        }
    }

    @Override
    public GridFsResource downloadFile(String fileId) throws IOException, InternalException {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "GridFsResource downloadFile(String fileId)" +
                    "-", e);
        }

    }

    private GridFSBucket getGridFs() throws InternalException {
        try {
            MongoDatabase db = mongoDbFactory.getDb();
            return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "GridFSBucket getGridFs()" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            Protocol protocol = repository.getById(id);
            protocol.setState(DMPConstants.STATUS_DELETED);
            repository.save(protocol);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }

    }
}
