package kz.beeset.med.admin.service;

import kz.beeset.med.admin.model.Guide;
import kz.beeset.med.admin.utils.error.InternalException;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IGuideService {
    List<Guide> getAllGuides() throws InternalException;
    Guide getGuideById(String id) throws InternalException;

    Guide setGuide(Guide guide) throws InternalException;

    String saveDocument(InputStream content, String mimeType, String fileName) throws InternalException;
    GridFsResource downloadDocument(String fileId) throws InternalException;

    String saveAvatar(InputStream content, String mimeType, String fileName) throws InternalException;
    GridFsResource downloadAvatar(String avatarId) throws InternalException;

    void deleteGuideById(String id) throws InternalException;
}
