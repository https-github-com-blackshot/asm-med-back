package kz.beeset.med.admin.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.admin.configs.MongoConfig;
import kz.beeset.med.admin.model.Guide;
import kz.beeset.med.admin.repository.GuideRepository;
import kz.beeset.med.admin.service.IGuideService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class GuideService implements IGuideService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuideService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());
    @Autowired
    GuideRepository guideRepository;
    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public List<Guide> getAllGuides() throws InternalException {
        try {
            return guideRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllResources", e);
        }
    }

    @Override
    public Guide getGuideById(String id) throws InternalException {
        try {
            return guideRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getGuideById", e);
        }
    }

    @Override
    public Guide setGuide(Guide guide) throws InternalException {
        try {
            return guideRepository.save(guide);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setGuide", e);
        }
    }

    @Override
    public String saveDocument(InputStream content, String mimeType, String fileName) throws InternalException{

        try {
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "file");
            metaData.put("kind", "file");
            metaData.put("mimeType", mimeType);

            return gridFsOperations.store(content, fileName, mimeType, metaData).toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:saveDocument", e);
        }
    }

    @Override
    public GridFsResource downloadDocument(String fileId) throws InternalException{
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:downloadFile", e);
        }

    }

    @Override
    public String saveAvatar(InputStream content, String mimeType, String fileName) throws InternalException {
        try {
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("kind", "avatar");
            metaData.put("mimeType", mimeType);

            return gridFsOperations.store(content, fileName, mimeType, metaData).toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:saveDocument", e);
        }
    }

    @Override
    public GridFsResource downloadAvatar(String avatarId) throws InternalException {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(avatarId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:downloadAvatar", e);
        }
    }

    @Override
    public void deleteGuideById(String id) throws InternalException {
        try {
            guideRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:deleteGuideById", e);
        }
    }

    private GridFSBucket getGridFs() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
    }
}
