package kz.beeset.med.admin.service;

import kz.beeset.med.admin.model.Tutorial;
import kz.beeset.med.admin.utils.error.InternalException;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ITutorialService {
    List<Tutorial> getAllTutorials() throws InternalException;
    Tutorial getTutorialById(String id) throws InternalException;

    Tutorial setTutorial(Tutorial tutorial) throws InternalException;

    String saveFile(InputStream content, String mimeType,String fileName) throws InternalException, IOException;
    GridFsResource downloadFile(String fileId);

    void deleteTutorialById(String id) throws InternalException;
}
