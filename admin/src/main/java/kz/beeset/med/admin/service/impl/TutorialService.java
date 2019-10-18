package kz.beeset.med.admin.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import kz.beeset.med.admin.configs.MongoConfig;
import kz.beeset.med.admin.model.Tutorial;
import kz.beeset.med.admin.repository.TutorialRepository;
import kz.beeset.med.admin.service.ITutorialService;
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
public class TutorialService implements ITutorialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TutorialService.class);

    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    TutorialRepository tutorialRepository;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private MongoConfig mongoConfig;

    @Override
    public List<Tutorial> getAllTutorials() throws InternalException {
        try {
            return tutorialRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllTutorials", e);
        }
    }

    @Override
    public Tutorial getTutorialById(String id) throws InternalException {
        try {
            return tutorialRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getTutorialById", e);
        }
    }

    @Override
    public Tutorial setTutorial(Tutorial tutorial) throws InternalException {
        try {
            LOGGER.info("tutorial: "+tutorial);
            return tutorialRepository.save(tutorial);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setTutorial", e);
        }
    }

    @Override
    public String saveFile(InputStream content, String mimeType, String fileName) throws IOException, InternalException {

        try{
            // Добавляем свою произвольную информацию к файлу
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("kind", "avatar");
            metaData.put("mimeType", mimeType);
            LOGGER.debug("[saveFile()] - mimeType: " + mimeType);
            return gridFsOperations.store(content, fileName , mimeType, metaData).toString();
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String saveAvatar(MultipartFile file)" +
                    "-", e);
        }
    }

    @Override
    public GridFsResource downloadFile(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
        return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));

    }

    @Override
    public void deleteTutorialById(String id) throws InternalException {
        try{
           tutorialRepository.deleteById(id);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void deleteTutorialById(String id)" +
                    "-", e);
        }
    }

    private GridFSBucket getGridFs() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
    }
}
